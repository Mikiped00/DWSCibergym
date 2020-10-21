package com.example.Lesson;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.example.Member.Member;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class LessonJsonSerializer extends JsonSerializer<Lesson> {
 
    @Override
    public void serialize(Lesson lesson, JsonGenerator jsonGenerator, 
      SerializerProvider serializerProvider) throws IOException, 
      JsonProcessingException {
  
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", String.valueOf(lesson.getId()));
        jsonGenerator.writeStringField("name", lesson.getName());
        jsonGenerator.writeStringField("description", lesson.getDescription());
        jsonGenerator.writeStringField("availablePlaces", String.valueOf(lesson.getAvailablePlaces()));
        jsonGenerator.writeStringField("begginingHour", String.valueOf(lesson.getBegginingHour()));
        jsonGenerator.writeStringField("begginingMinutes", String.valueOf(lesson.getBegginingMinutes()));
        jsonGenerator.writeStringField("endingHour", String.valueOf(lesson.getEndingHour()));
        jsonGenerator.writeStringField("endingMinutes", String.valueOf(lesson.getEndingMinutes()));
        jsonGenerator.writeArrayFieldStart("reservedMember");
        if (!lesson.getReservedMember().isEmpty()) {
            for (Member member : lesson.getReservedMember()) {
        		jsonGenerator.writeString(member.getName());
        	}
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}