package com.jasper.core.contractor.handle;

import org.springframework.context.ApplicationEvent;

public class UpdateFinishedEvent extends ApplicationEvent {

    public UpdateFinishedEvent(Object source) {
        super(source);
    }
}
