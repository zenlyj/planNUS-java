<?php
    // require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # usage http://116.14.246.142/importnusmods.php?url=https://nusmods.com/timetable/sem-1/share?CS1101S=REC:09,TUT:09B,LEC:2|ES1103=SEC:C01|IS1103=SEC:1,TUT:18|MA1301=LEC:1,TUT:3|PC1221=TUT:6,LEC:1,LAB:9
    # replace url & with | for delims
    try {
        $message = new \stdClass();
        $message->success = false;
        if (isset($_GET['url'])) {
            $resultArr = array();
            $params = explode('/share?', $_GET['url']);
            if (!(isset($params[0]) && isset($params[1]))) {
                throw new Exception("");
            }
            $modules = explode('|', $params[1]);
            $baseURL = "https://api.nusmods.com/v2/2020-2021/modules/";
            foreach ($modules as $module) {
                $moduleCode = explode('=', $module)[0];
                $moduleClass = explode(',', explode('=', $module)[1]);
                $fetchURL = $baseURL . $moduleCode . ".json";
                $json = file_get_contents($fetchURL);
                $obj = json_decode($json);
                $timetable = $obj->semesterData[0]->timetable;
                $classDetail = new \stdClass();
                $classDetail->module = $moduleCode;
                $classDetail->data = array_filter($timetable, function($class) {
                    global $moduleClass;
                    return in_array(strtoupper(substr($class->lessonType, 0, 3) . ":" . $class->classNo), array_map(function($modClass) {
                        return strtoupper($modClass);
                    }, $moduleClass), true);
                });
                $resultArr[] = $classDetail;
            }
            $taskArr = array();
            foreach ($resultArr as $result) {
                foreach ($result->data as $singleClass) {
                    foreach ($singleClass->weeks as $week) {
                        $task = new \stdClass();
                        $task->id = changeToKey($singleClass->day, $singleClass->startTime);
                        $task->taskPresent = 1;
                        $task->taskName = $result->module . " (" . $singleClass->lessonType .")";
                        $task->module = $result->module;
                        $task->timeFrom = $singleClass->startTime;
                        $task->timeTo = $singleClass->endTime;
                        $task->description = "";
                        $task->week = $week;
                        $taskArr[] = $task;
                    }
                }
            }
            $message->taskArr = $taskArr;
            $message->success = true;
            echo json_encode($message);
        } else {
            $message->success = false;
            echo json_encode($message);
        }
    } catch(Exception $e) {
        $message->success = false;
        echo json_encode($message);
    }

    function changeToKey($day, $time) {
        $to_Day = "";
        $day = ucfirst(strtolower($day));
        if ($day == "Monday") {
            $to_Day = "MON";
        } else if ($day == "Tuesday") {
            $to_Day = "TUES";
        } else if ($day == "Wednesday") {
            $to_Day = "WED";
        }  else if ($day == "Thursday") {
            $to_Day = "THURS";
        } else if ($day == "Friday") {
            $to_Day = "FRI";
        } else if ($day == "Saturday") {
            $to_Day = "SAT";
        } else if ($day == "Sunday") {
            $to_Day = "SUN";
        }
        return $to_Day . ( $time / 100 - 7);
    }
?>
