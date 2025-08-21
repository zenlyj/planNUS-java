<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # usage http://116.14.246.142/workscheduler.php?nusnet=e0407306&week=1&modules=CS1101S&Monday-start=0900&Monday-end=1900&Monday-hours=5&Tuesday-start=0900&Tuesday-end=1900&Tuesday-hours=5&Wednesday-start=0900&Wednesday-end=1900&Wednesday-hours=5&Thursday-start=0900&Thursday-end=1900&Thursday-hours=5&Friday-start=0900&Friday-end=1900&Friday-hours=5&Saturday-start=0900&Saturday-end=1900&Saturday-hours=4&Sunday-start=0900&Sunday-end=1900&Sunday-hours=4
    try {
        if (isset($_GET['modules']) && isset($_GET['nusnet']) && isset($_GET['week'])) {
            $nusnet = $_GET['nusnet'];
            $week = $_GET['week'];
            $inavailable = array();
            $mondayStart = $_GET['Monday_start'];
            $mondayEnd = $_GET['Monday_end'];
            $inavailable["monday"] = array();
            $inavailable["monday"][] = [$mondayEnd, '2400'];
            $inavailable["monday"][] = ['0000', $mondayStart];
            $tuesdayStart = $_GET['Tuesday_start'];
            $tuesdayEnd = $_GET['Tuesday_end'];
            $inavailable["tuesday"] = array();
            $inavailable["tuesday"][] = [$tuesdayEnd, '2400'];
            $inavailable["tuesday"][] = ['0000', $tuesdayStart];
            $wednesdayStart = $_GET['Wednesday_start'];
            $wednesdayEnd = $_GET['Wednesday_end'];
            $inavailable["wednesday"] = array();
            $inavailable["wednesday"][] = [$wednesdayEnd, '2400'];
            $inavailable["wednesday"][] = ['0000', $wednesdayStart];
            $thursdayStart = $_GET['Thursday_start'];
            $thursdayEnd = $_GET['Thursday_end'];
            $inavailable["thursday"] = array();
            $inavailable["thursday"][] = [$thursdayEnd, '2400'];
            $inavailable["thursday"][] = ['0000', $thursdayStart];
            $fridayStart = $_GET['Friday_start'];
            $fridayEnd = $_GET['Friday_end'];
            $inavailable["friday"] = array();
            $inavailable["friday"][] = [$fridayEnd, '2400'];
            $inavailable["friday"][] = ['0000', $fridayStart];
            $saturdayStart = $_GET['Saturday_start'];
            $saturdayEnd = $_GET['Saturday_end'];
            $inavailable["saturday"] = array();
            $inavailable["saturday"][] = [$saturdayEnd, '2400'];
            $inavailable["saturday"][] = ['0000', $saturdayStart];
            $sundayStart = $_GET['Sunday_start'];
            $sundayEnd = $_GET['Sunday_end'];
            $inavailable["sunday"] = array();
            $inavailable["sunday"][] = [$sundayEnd, '2400'];
            $inavailable["sunday"][] = ['0000', $sundayStart];
            $modules = explode(',', $_GET['modules']);
            $baseURL = "https://api.nusmods.com/v2/2019-2020/modules/";
            $modules = explode(',', $_GET['modules']);
            $weekTasks = getWeekTasks($conn, $modules);
            foreach ($weekTasks as $module) {
                foreach ($module as $task) {
                    $day = getDay($task['id']);
                    $inavailable[$day][] = [$task['timeFrom'], $task['timeTo']];
                }
            }
            usort($inavailable["monday"], "compare");
            usort($inavailable["tuesday"], "compare");
            usort($inavailable["wednesday"], "compare");
            usort($inavailable["thursday"], "compare");
            usort($inavailable["friday"], "compare");
            usort($inavailable["saturday"], "compare");
            usort($inavailable["sunday"], "compare");
            $allocatedSlot = getAllocatedSlot(getAvailability($inavailable));
            $days = ["monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"];
            $allocatedSlotSize = 0;
            foreach ($allocatedSlot as $day => $slot) {
                $allocatedSlotSize += count($allocatedSlot[$day]);
            }
            $tasksArr = array();
            foreach ($modules as $module) {
                $moduleHours = getModuleHours($module);
                while (($moduleHours > 0) && ($allocatedSlotSize > 0)) {
                    $index = mt_rand(0, count($days) - 1);
                    $key = $days[$index];
                    if (count($allocatedSlot[$key]) > 0) {
                        $task = array();
                        $assignedSlot = array_shift($allocatedSlot[$key]);
                        $assignedSlotDuration = ($assignedSlot[1] - $assignedSlot[0]) / 100;
                        if ($assignedSlotDuration <= $moduleHours) {
                            $task['timeFrom'] = $assignedSlot[0];
                            $task['timeTo'] = $assignedSlot[1];
                            $allocatedSlotSize--;
                        } else {
                            $task['timeFrom'] = $assignedSlot[0];
                            $task['timeTo'] = $assignedSlot[0] + $moduleHours * 100;
                            $leftoverSlot = [$assignedSlot[0] + $moduleHours * 100 , $assignedSlot[1]];
                            array_unshift($allocatedSlot[$key], $leftoverSlot); 
                        }
                        $task['week'] = $week;
                        $task['nusnet'] = $nusnet;
                        $task['taskPresent'] = true;
                        $task['taskName'] = $module . ' (etc)';
                        $task['module'] = $module;
                        $task['description'] = 'Automated Scheduler';
                        $task['id'] = changeToKey($key, $assignedSlot[0]);
                        $tasksArr[] = $task;
                        $moduleHours -= $assignedSlotDuration;

                    } else if (count($allocatedSlot[$key]) == 0) {
                        \array_splice($days, $index, 1);
                    }
                }
            }
            echo json_encode($tasksArr);
        } else {
            echo "failed";
        }
    } catch(ErrorException $e) {
        echo "failed";
        echo $e->getMessage();
    }

    function getWeekTasks($conn, $modules) {
        try {
            $resultArr = array();
            if (isset($_GET['nusnet']) && isset($_GET['week'])) {
                foreach ($modules as $module){
                    $querySearch = sprintf("SELECT * from `task` where nusnet = '%s' and week = '%s'", $_GET['nusnet'], $_GET['week']);
                    $result = $conn->query($querySearch);
                    $resultArr[$module] = array();
                    if ($result->num_rows > 0 ) {
                        while ($row = $result->fetch_assoc()) {
                            $resultArr[$module][] = $row;
                        }
                    }
                }
            } 
            return $resultArr;
        } catch(ErrorException $e) {
            return array();
        }
    }

    function getDay($id) {
        $shortDay = strtoupper(substr($id, 0 , 3));
        if ($shortDay == "MON") {
            return "monday";
        } else if ($shortDay == "TUE") {
            return "tuesday";
        } else if ($shortDay == "WED") {
            return "wednesday";
        } else if ($shortDay == "THU") {
            return "thursday";
        } else if ($shortDay == "FRI") {
            return "friday";
        } else if ($shortDay == "SAT") {
            return "saturday";
        } else if ($shortDay == "SUN") {
            return "sunday";
        }
    }

    function compare($a, $b) {
        return ($a[1] < $b[1]) ? -1 : 1;
    }

    function getAvailability($inavailable) {
        $result = array();
        foreach ($inavailable as $day => $task) {
            $result[$day] = array();
            for ($i = 0; $i < count($inavailable[$day]); $i++) {
                if ($i == (count($inavailable[$day]) - 1)) {
                   $availableSection = [$inavailable[$day][$i][1], $inavailable[$day][0][0]];
                } else {
                    $availableSection = [$inavailable[$day][$i][1], $inavailable[$day][$i + 1][0]];
                }
                if ($availableSection[0] == $availableSection[1]) {
                    continue;
                } else if ($availableSection[0] > $availableSection[1]) {
                    continue;
                }
                $result[$day][] = $availableSection;
            }
            $result[$day] = array_values(array_filter($result[$day], function($x) use ($day) {
                global $mondayStart, $mondayEnd, $tuesdayStart, $tuesdayEnd, $wednesdayStart, $wednesdayEnd,
                $thursdayStart, $thursdayEnd, $fridayStart, $fridayEnd, $saturdayStart, $saturdayEnd, $sundayStart, $sundayEnd;
                $start = $day . 'Start';
                $end = $day . 'End';
                if ($x[0] < ${$start} || $x[1] < ${$start}) {
                    return false;
                } else if ($x[0] > ${$end}) {
                    return false;
                } else if ($x[1] < ${$start}) {
                    return false;
                } else {
                    return true;
                }
            }));
        }
        return $result;
    }

    function getAllocatedSlot($available) {
        $mondayHours = $_GET['Monday_hours'];
        $tuesdayHours = $_GET['Tuesday_hours'];
        $wednesdayHours = $_GET['Wednesday_hours'];
        $thursdayHours = $_GET['Thursday_hours'];
        $fridayHours = $_GET['Friday_hours'];
        $saturdayHours = $_GET['Saturday_hours'];
        $sundayHours = $_GET['Sunday_hours'];
        $result = array();
        foreach ($available as $day => $availableSection) {
            $result[$day] = array();
            $dayAvailableSections = $available[$day];
            usort($dayAvailableSections, "compareHours");
            for ($i = 0; $i < count($dayAvailableSections); $i++) {
                $slotDuration = ($dayAvailableSections[$i][1] - $dayAvailableSections[$i][0]) / 100;
                $dayHours = $day . "Hours";
                if (${$dayHours} == 0) {
                break;
                } else if (${$dayHours} >= $slotDuration) {
                    ${$dayHours} -= $slotDuration;
                    $result[$day][] = $dayAvailableSections[$i];
                } else if (${$dayHours} < $slotDuration) {
                    $start = $dayAvailableSections[$i][0] / 100;
                    $end = ($dayAvailableSections[$i][1] / 100);
                    $newStart = mt_rand($start, $end - ${$dayHours}) * 100;
                    $newEnd = $newStart + (${$dayHours} * 100);
                    $result[$day][] = [$newStart, $newEnd];
                    ${$dayHours} = 0;
                }
                
            }
        }
        return $result;
    }

    function compareHours($a, $b) {
        return (($a[0] - $a[1]) < ($b[0] -$b[1])) ? -1 : 1;
    }

    function getModuleHours($module){
        global $conn;
        $querySearch = sprintf("SELECT * from `task` where nusnet = '%s' and week = '%s' and module = '%s'", $_GET['nusnet'], $_GET['week'], $module);
        $result = $conn->query($querySearch);
        $existingWorkHours = 0;
        if ($result->num_rows > 0 ) {
            while ($row = $result->fetch_assoc()) {
                $existingWorkHours += ($row['timeTo'] - $row['timeFrom']) / 100;
            }
        }
        $baseURL = "https://api.nusmods.com/v2/2019-2020/modules/";
        $fetchURL = $baseURL . $module . ".json";
        $file_headers = @get_headers($fetchURL);
        if ($file_headers[0] == 'HTTP/1.1 404 Not Found') {
            $obj = new \stdclass();
            $obj->moduleCredit = 0;
        } else {
            $json = file_get_contents($fetchURL);
            $obj = json_decode($json);
        }
        $workHours = $obj->moduleCredit * 2.5 - ($existingWorkHours >= $obj->moduleCredit * 2.5 ? 0 : $existingWorkHours);
        return $workHours;
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