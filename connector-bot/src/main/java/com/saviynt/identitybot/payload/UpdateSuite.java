package com.saviynt.identitybot.payload;

import com.google.gson.JsonArray;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.util.List;

@Getter
@Setter

public class UpdateSuite {

    private Long exeType;

    private String browserType;

    private String pExexution;

    private List job;


}
