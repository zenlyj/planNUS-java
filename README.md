# Tools and Technologies
1. ReactJS
2. Spring Boot, Spring Security
3. PostgreSQL
4. [NUSMods API](https://api.nusmods.com/v2/)

# Features

## Timetable Scheduling

![timetable_overview](https://raw.githubusercontent.com/zenlyj/PlanNUS-java/master/readme_images/timetable_overview.PNG)

![timetable_event](https://raw.githubusercontent.com/zenlyj/PlanNUS-java/master/readme_images/timetable_event.PNG)

A fully customizable timetable that supports multi-week planning. Users are able to create, read, update and delete events on the timetable.

## Deadline Tracking

![deadline_overview](https://raw.githubusercontent.com/zenlyj/PlanNUS-java/master/readme_images/deadline_overview.PNG)

List of deadlines to remind users of outstanding assignment/project submissions. Similar to the timetable, CRUD operations are supported as well.

## Diary

![diary_overview](https://raw.githubusercontent.com/zenlyj/PlanNUS-java/master/readme_images/diary_overview.PNG)

![diary_view](https://raw.githubusercontent.com/zenlyj/PlanNUS-java/master/readme_images/diary_view.PNG)

Calendar that provides an overview of each day. After selecting a day, users can view tasks and deadlines, and write diary entries to log their thoughts for the day.

![diary_taskview](https://raw.githubusercontent.com/zenlyj/PlanNUS-java/master/readme_images/diary_taskview.PNG)

Users can also mark tasks as complete/incomplete to keep track of their progress.

## Statistics

![stats_overview](https://raw.githubusercontent.com/zenlyj/PlanNUS-java/master/readme_images/stats_overview.PNG)

In NUS, each module has a recommended weekly workload (in hours). To help users track progress for each module, the statistics page displays a bar chart to visualize the recommended, plotted, completed work hours for each module taken by the user. Plotted work hours corresponds to the total number of hours attained from summing up all events in the timetable. Completed work hours corresponds to the total number of hours attained from summing up all events that are marked as completed in the diary.

## Import from NUSMods

![nusmods_share](https://raw.githubusercontent.com/zenlyj/PlanNUS-java/master/readme_images/nusmods_share.PNG)

![nusmods_import](https://raw.githubusercontent.com/zenlyj/PlanNUS-java/master/readme_images/nusmods_import.PNG)

PlanNUS supports integration with popular module planning website [NUSMods](https://nusmods.com/). After selecting their preferred modules to take, users can export the timetable from NUSMods to PlanNUS to further customize their timetables.

## Authentication and Authorization

The application supports basic authentication and authorization using Spring Security. Upon authentication, users will be assigned JSON Web Token (JWT) access and refresh tokens. Access tokens are used for user authorization for application usage. Upon expiry of access tokens, refresh tokens are used to renew access tokens for continued application usage.

# Further Extensions

1. Constraint based timetable scheduling. Utilize NUSMods to design a timetable automatically based on users' preferences.
2. Import/Export PlanNUS timetable. Allow friends to synchronize their schedules to study together.
