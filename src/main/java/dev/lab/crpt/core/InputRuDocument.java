package dev.lab.crpt.core;

public record InputRuDocument(
        String docType,
        String producerInn,
        String productionDate,
        Object products
) {}
