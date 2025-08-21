package com.orbital.planNUS.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class NUSModClient {
  private final String base = "https://api.nusmods.com/v2/";
  private final String moduleEndpoint = "%s%s/modules/%s.json";

  private final ObjectMapper objectMapper;
  private final HttpClient httpClient;

  public NUSModClient() {
    this.objectMapper = new ObjectMapper();
    this.httpClient = HttpClient.newHttpClient();
  }

  public List<Lesson> enrichAndExtendLessons(
      List<Lesson> lessons, String academicYear, int semesterNum) {
    List<Lesson> extendedLessons = new ArrayList<>();

    for (Lesson lesson : lessons) {
      try {
        String module = lesson.getModuleCode();
        var uri = new URI(String.format(moduleEndpoint, base, academicYear, module));
        String response = fetchBody(uri);
        ArrayNode timetable = getTimetable(response, semesterNum);
        int workload = getWorkLoadInHours(response);
        var isFilled = false;

        for (JsonNode lessonNode : timetable) {
          String lessonId = lessonNode.get("classNo").asText();
          String lessonType = lessonNode.get("lessonType").asText();
          String startTime = lessonNode.get("startTime").asText();
          String endTime = lessonNode.get("endTime").asText();
          String day = lessonNode.get("day").asText();

          if (!lessonType.equals(lesson.getLessonType())
              || !lessonId.equals(lesson.getLessonId())) {
            continue;
          }

          if (isFilled) {
            var additionalLesson = new Lesson(lesson);
            additionalLesson.setStartTime(startTime);
            additionalLesson.setEndTime(endTime);
            additionalLesson.setDay(day);
            extendedLessons.add(additionalLesson);
          } else {
            List<Integer> weeks = getWeeks(lessonNode);
            lesson.setStartTime(startTime);
            lesson.setEndTime(endTime);
            lesson.setWorkloadHours(workload);
            lesson.setWeeks(weeks);
            lesson.setDay(day);
          }
          isFilled = true;
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    return Stream.concat(lessons.stream(), extendedLessons.stream()).toList();
  }

  public int mapModuleToWorkload(String module, String academicYear) {
    try {
      var uri = new URI(String.format(moduleEndpoint, base, academicYear, module));
      String response = fetchBody(uri);
      return getWorkLoadInHours(response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private int getWorkLoadInHours(String response) throws JsonProcessingException {
    JsonNode node = objectMapper.readTree(response);
    ArrayNode workloadNode = (ArrayNode) node.get("workload");

    var workload = 0;
    for (int i = 0; i < workloadNode.size(); i++) {
      workload += workloadNode.get(i).asInt();
    }
    return workload;
  }

  private ArrayNode getTimetable(String response, int semesterNum) throws JsonProcessingException {
    JsonNode node = objectMapper.readTree(response);
    var semesterData = (ArrayNode) node.get("semesterData");
    JsonNode data = semesterData.get(semesterNum - 1);
    return (ArrayNode) data.get("timetable");
  }

  private List<Integer> getWeeks(JsonNode lessonNode) {
    var weeks = (ArrayNode) lessonNode.get("weeks");
    return weeks.valueStream().map(JsonNode::asInt).toList();
  }

  private String fetchBody(URI uri) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() >= 200 && response.statusCode() < 300) {
      return response.body();
    } else {
      throw new IOException("Failed to fetch %s: %d".formatted(uri, response.statusCode()));
    }
  }
}
