## Local development instructions

This document is suitable for people with a certain foundation , you need to have SpringCloud/SringBoot development foundation , Vue/Npm front-end development foundation , Mysql foundation , Mongodb foundation , Redis foundation , Linux foundation . Generally speaking, most Java programmers have these abilities.

Meanwhile, if you need to connect to the blockchain and operate/data access to the blockchain wallet, you also need to have some blockchain related basics, including but not limited to: building nodes for Bitcoin/Ether, etc., RPC access to blockchain nodes, bitcoin wallet fundamentals, ethereum wallet fundamentals, and so on. Generally speaking, you don't need to be particularly proficient in the blockchain operation process, just need to have some understanding of the blockchain operation principle.

This project is separated from the front and back ends, so if you are a full-stack programmer, this project should be debugged through very quickly.
If you have any questions you can add QQ: 390330302, I will give some technical assistance (white questions please bypass).

## About Framework development

00_Framework folder under the project is a collection of all the services through SpringCloud's microservices development model for development, you can open the entire project through Eclipse, my development tools version is as follows:

> Eclipse Java EE IDE for Web Developers.
> Version: Photon Release (4.8.0)
> Build id: 20180619-1200

First, install the Lombok plugin for the development tool.

After opening the project with Eclipse, your development project directory should look like the following:
![Developer Interface](<https://images.gitee.com/uploads/images/2020/0324/104050_1d27dea9_2182501.png> “QQ Screenshot 20200324104038.png”)

When compiling the project, please compile core as well as xxx-core project first, otherwise it will prompt missing classes (this project uses JPA QueryDsl to realize table operation).

Each project has its own service port (in the configuration file), you can call the service directly through (IP: port), you can also call the service through the gateway (Cloud project, port 7000), you can also configure the Nginx reverse proxy for the service call (production environments use this method)

## On the background management Web development

The configuration file is in src/config/api.js, where you can configure your Admin service IP port, because the backend management web will only call one Admin service, so, you can choose to directly specify to the Admin service IP: port.

Also, you need to modify the access configuration in the src/service/http.js file.

Startup mode: you can hot start the project by npm run dev, and compile the deployment file by npm run build.

## About Frontend Web End Development

Frontend web end (PC) because you want to access different services, like personal information need to call the service provided by Ucenter, transaction need to call the service provided by Exchange-api, so you need to provide services for the frontend by way of gateway.
The configuration file is in: src/main.js, you can modify Vue.prototype.rootHost and Vue.prototype.host to realize the call to the back-end services.

## About the database (MySQL)

The database file is in the 00_Framework/sql folder, db_patch is provided, this file only provides some basic data (menu tree, administrator permissions, etc.), the other database tables will be automatically updated to the database when the jar package is run for the first time, the configuration items are in:

> #jpa

> spring.jpa.show-sql=true

> spring.data.jpa.repositories.enabled=true

> spring.jpa.hibernate.ddl-auto=update

If you do not want the database tables to be dynamically updated with the Java class entities, you can choose to modify the configuration item: spring.jpa.hibernate.ddl-auto=update

## Environment Configuration

The system runs on Mongodb, Redis, MySQL, kafka, AliCloud OSS, so you need to prepare these basic services.

## Common compilation problems (Error)

1. When starting ucenter-api, the following error is prompted:
Error creating bean with name 'enableRedisKeyspaceNotificationsInitializer' defined in class path resource
Reason: The annotation @EnableRedisHttpSession is used to enable Redis to manage sessions in a centralized way, and in this way, Redis is required to enable Keyspace Notifications, which is turned off by default. This feature has a parameter to control it, notify-keyspace-events, change its value to Egx.
Tencent cloud in the redis parameter notify-keyspace-events modify method: into the instance -> parameters, modify can be.

2. Running jar package prompts Failed to get nested archive for entry BOOT.......
Tip missing spark.xxxx.DB class, this is because the compiler did not copy the spark-core.jar to the compressed package, this time you need to modify the pom.xml file, append:

```bash
    <configuration>
        <includeSystemScope>true</includeSystemScope
    </configuration>
```

append location: <artifactId>spring-boot-maven-plugin</artifactId>, and <executions> level
Then delete the jar package under target and regenerate it. It is better to run maven clean and then maven install.

3. org.apache.shiro.authz.UnauthorizedException: Subject does not have permission [system:announcement:deletes]
This error is caused by the operation not having permission, check if the method of the operation is the same as the one in the database.
The first step is to check the requested link through the browser F12, which usually shows 404.
The second step is to check whether there is a link to access in the admin_permission table in the database.

4. market.jar startup failure, prompt connection refuesed and other errors, first use netstat -tunlp to see if port 6005 (that is, exchange) is listening, if not, it means that exchange is not fully started.
The main reason for slow startup of exchange is the slow loading of unfinished orders during initialization. When initializing, you need to load the order transaction details from mangodb, which is very huge data.

## Some configuration item descriptions

1、market/application.properties

## Whether second referrer coin fee commission is awarded (true: open false: not open)

 second.referrer.award=false

2、ucenter-api/application.properties

# system(for sending emails)

 spark.system.work-id=1
 spark.system.data-center-id=1
 spark.system.host=
 spark.system.name=

# Referral registration rewards, 1=Referrals must be real-name authenticated to receive rewards, otherwise there is no limit, to maintain consistency with the configuration inside the admin module

 commission.need.real-name=1
 #Whether to enable second-level rewards for referral registration (1=on, 0=off)
 commission.promotion.second-level=0

# The prefix of the individual promotion link, returned to the client with the login interface. The client side is connected to the promotion code to form the personal promotion link. Required if there is a promotion registration feature

 person.promotion.prefix=<http://www.bizzan.com/#/register?agent=>
 #Transfer interface address
 transfer.url=
 transfer.key=
 transfer.smac=

## Nginx configuration

 **Note:**

1. api.xxxx.com and <www.xxxx.com> are not compatible.

2. api.xxxx.com is required to support websockets.

===========================

Dependent environment

```bash
yum install -y wget  
yum install -y vim-enhanced  
yum install -y make cmake gcc gcc-c++  
yum install -y pcre pcre-devel
yum install -y zlib zlib-devel
yum install -y openssl openssl-devel
```

`wget http://nginx.org/download/nginx-1.12.2.tar.gz`

Compile and install

```bash
tar -zxvf nginx-1.12.2.tar.gz 
cd nginx-1.12.2
```

```bash
./configure \
--prefix=/usr/local/nginx \
--pid-path=/var/run/nginx/nginx.pid \
--lock-path=/var/lock/nginx.lock \
--error-log-path=/var/log/nginx/error.log \
--http-log-path=/var/log/nginx/access.log \
--with-http_gzip_static_module \
--http-client-body-temp-path=/var/temp/nginx/client \
--http-proxy-temp-path=/var/temp/nginx/proxy \
--http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
--http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
--with-http_stub_status_module \
--with-http_ssl_module \
--http-scgi-temp-path=/var/temp/nginx/scgi \
--with-stream --with-stream_ssl_module

make 
make install
```

# About accessing services

You can see each service through Eruka's service scheduling center (<<http://localhost:7000>), or call the service through the zuul unified gateway, for convenience, the way I locally developed is to use nginx reverse proxy for various services, the configuration reference is as follows:>

```bash
    server_name locahost;
    location /market {
        client_max_body_size    5m;
        proxy_pass http://localhost:6004;
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Scheme $scheme;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
    location /exchange {
        client_max_body_size    5m;
        proxy_pass http://localhost:6003;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    location /uc {
        client_max_body_size    5m;
        proxy_pass http://localhost:6001;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    location /admin {
        client_max_body_size    5m;
        proxy_pass http://localhost:6010;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    location /chat {
        client_max_body_size    5m;
        proxy_pass http://localhost:6008;
        proxy_set_header Host $host;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header X-Real-IP $remote_addr;
    }
```
