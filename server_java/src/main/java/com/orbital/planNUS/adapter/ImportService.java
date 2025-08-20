package com.orbital.planNUS.adapter;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportService {
  private final ShareLinkParser shareLinkParser;
  private final NUSModClient nusModClient;

  public ImportService(ShareLinkParser shareLinkParser, NUSModClient nusModClient) {
    this.shareLinkParser = shareLinkParser;
    this.nusModClient = nusModClient;
  }

  public List<Lesson> importLessons(String link, String academicYear, int semesterNum) {
    var lessons = shareLinkParser.parse(link);
    return nusModClient.enrichAndExtendLessons(lessons, academicYear, semesterNum);
  }

  public int getModuleWorkloadInHours(String module, String academicYear) {
    return nusModClient.mapModuleToWorkload(module, academicYear);
  }
}
