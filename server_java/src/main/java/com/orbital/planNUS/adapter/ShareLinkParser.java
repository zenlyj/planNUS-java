package com.orbital.planNUS.adapter;

import com.orbital.planNUS.dto.lesson.Lesson;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ShareLinkParser {
  private final Map<String, String> classShorthandToFull =
      Map.of(
          "LEC",
          "Lecture",
          "TUT",
          "Tutorial",
          "LAB",
          "Laboratory",
          "SEC",
          "Sectional Teaching",
          "REC",
          "Recitation");

  public List<Lesson> parse(String link) {
    try {
      var uri = new URI(link);
      String query = uri.getQuery();
      if (query == null) {
        return Collections.emptyList();
      }
      String[] modules = query.split("&");
      return Arrays.stream(modules).map(this::mapModuleToLessons).flatMap(List::stream).toList();
    } catch (URISyntaxException e) {
      throw new RuntimeException("Invalid URI");
    }
  }

  private List<Lesson> mapModuleToLessons(String module) {
    String[] moduleInfo = module.split("=");
    var moduleCode = moduleInfo[0];
    var moduleClasses = moduleInfo[1];

    return Arrays.stream(moduleClasses.split(","))
        .map(
            klass -> {
              String[] classInfo = klass.split(":");
              var classType = classShorthandToFull.get(classInfo[0]);
              var classId = classInfo[1];
              return new Lesson(moduleCode, classType, classId);
            })
        .toList();
  }
}
