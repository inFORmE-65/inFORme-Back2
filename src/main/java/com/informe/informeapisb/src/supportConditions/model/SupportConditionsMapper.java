package com.informe.informeapisb.src.supportConditions.model;

import java.util.List;

public interface SupportConditionsMapper {

    // SELECT SVC_ID FROM umchwanDB.supportConditions

    // @Select("select * from supportConditions")
    public List<SupportConditionsData> getConditionsList();
}
