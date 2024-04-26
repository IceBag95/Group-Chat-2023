package com.example.diet2;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Message {

    String name, message;
    public Message(String name, String message){
        this.name = name;
        this.message = message;
    }

}
