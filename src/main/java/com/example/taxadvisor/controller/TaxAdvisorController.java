package com.example.taxadvisor.controller;

import com.example.taxadvisor.model.TaxAdviceRequest;
import com.example.taxadvisor.model.TaxAdviceResponse;
import com.example.taxadvisor.service.TaxAdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax-advisor")
public class TaxAdvisorController {

    @Autowired
    private TaxAdvisorService taxAdvisorService;

    @PostMapping("/recommendations")
    public TaxAdviceResponse getRecommendations(@RequestBody TaxAdviceRequest request) {
        return taxAdvisorService.getTaxAdvice(request);
    }
}
