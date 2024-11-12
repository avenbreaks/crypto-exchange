package com.bizzan.bitrade.model.update;

import com.bizzan.bitrade.ability.UpdateAbility;
import com.bizzan.bitrade.entity.DataDictionary;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Hevin  E-mail:bizzanhevin@gmail.com
 * @Title: ${file_name}
 * @Description:
 * @date 2019/4/1214:46
 */
@Data
public class DataDictionaryUpdate implements UpdateAbility<DataDictionary> {
    @NotBlank
    private String value;
    private String comment;

    @Override
    public DataDictionary transformation(DataDictionary dataDictionary) {
        dataDictionary.setValue(value);
        dataDictionary.setComment(comment);
        return dataDictionary;
    }
}
