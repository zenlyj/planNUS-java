package com.orbital.planNUS.service;

import com.orbital.planNUS.adapter.NUSModClient;
import com.orbital.planNUS.dto.lesson.Lesson;
import com.orbital.planNUS.adapter.ShareLinkParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportService {
  private ShareLinkParser shareLinkParser;
  private NUSModClient nusModClient;

  public ImportService(ShareLinkParser shareLinkParser, NUSModClient nusModClient) {
    this.shareLinkParser = shareLinkParser;
    this.nusModClient = nusModClient;
  }

  public List<Lesson> importLessons(String link, String academicYear, int semesterNum) {
    List<Lesson> lessons = shareLinkParser.parse(link);
    return nusModClient.enrichAndExtendLessons(lessons, academicYear, semesterNum);
  }

  public int getModuleWorkloadInHours(String module, String academicYear) {
    return nusModClient.mapModuleToWorkload(module, academicYear);
  }
}
