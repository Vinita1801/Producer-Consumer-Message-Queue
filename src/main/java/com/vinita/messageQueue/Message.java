package com.vinita.messageQueue;

public class Message {
    private final String employeeName;

    public String getEmployeeName() { return employeeName; }

    public Message(String employeeName){ this.employeeName = employeeName; }

    @Override
    public String toString(){
        return "Message{" + "employeeName:'" + employeeName + '\'' + "}";
    }
}
