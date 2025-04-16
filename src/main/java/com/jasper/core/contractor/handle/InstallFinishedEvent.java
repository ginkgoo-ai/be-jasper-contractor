package com.jasper.core.contractor.handle;

import org.springframework.context.ApplicationEvent;

public class InstallFinishedEvent extends ApplicationEvent {

    public InstallFinishedEvent(Object source) {
        super(source);
    }
}
