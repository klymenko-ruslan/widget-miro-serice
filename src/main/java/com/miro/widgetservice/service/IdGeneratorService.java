package com.miro.widgetservice.service;

import org.springframework.stereotype.Service;

@Service
public class IdGeneratorService {
    private long sequenceNumber = 1;

    public synchronized long getNextId() {
        return sequenceNumber++;
    }

}
