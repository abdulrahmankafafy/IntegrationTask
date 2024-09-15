package com.kafafy.integrationFinal.controller;

import com.kafafy.integrationFinal.entity.InputEntity;
import com.kafafy.integrationFinal.model.FinalOutputModel;
import com.kafafy.integrationFinal.service.IntegrationServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Mocksss", description = "the operation is for calling the mocks")
public class MyController {

    @Autowired
    private IntegrationServices integrationServices;

    @Operation(summary = "Process input data", description = "Processes the input data by making SOAP and REST service calls")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed the request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @RequestMapping(value = "/processRequest", method = RequestMethod.POST)
    public FinalOutputModel processRequest(@RequestBody InputEntity inputEntity) {
        return integrationServices.processInput(inputEntity);
    }
}
