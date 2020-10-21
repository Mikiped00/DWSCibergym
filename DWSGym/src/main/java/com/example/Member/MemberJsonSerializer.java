package com.example.Member;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class MemberJsonSerializer extends JsonSerializer<Member> {
 
    @Override
    public void serialize(Member member, JsonGenerator jsonGenerator, 
      SerializerProvider serializerProvider) throws IOException, 
      JsonProcessingException {
  
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", String.valueOf(member.getId()));
        jsonGenerator.writeStringField("dni", member.getDni());
        jsonGenerator.writeStringField("name",member.getName());
        jsonGenerator.writeStringField("email", member.getEmail());
        jsonGenerator.writeStringField("fee", String.valueOf(member.getFee())+" €");
        //jsonGenerator.writeStringField("fee", String.valueOf(member.getFee()));
        if (member.getMembersLesson()!= null) {
            jsonGenerator.writeStringField("membersLesson", member.getMembersLesson().getName());
        } else {
            jsonGenerator.writeStringField("membersLesson", "No Lesson");
        }
        jsonGenerator.writeEndObject();
    }
}