<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # params
    # nusnet:str, id: str (MON1, MON2, THURS4, etc.), taskPresent: true, taskName: str, module: str, timeFrom: str (24hr format), timeTo: str(24hr format), description: str
    # usage http://116.14.246.142/addtask.php?nusnet=e0407306&id=WED1&taskPresent=true&taskName=ABC&module=cp2106&timeFrom=1600&timeTo=1700&description=xyz&week=5
    try {
        $message = new \stdClass();
        $message->success = false;
        if (isset($_GET['nusnet']) && isset($_GET['id'])) {
            $date = computeDate($_GET['id'], $_GET['week']);
            $querySearch = sprintf("SELECT * FROM `diary` WHERE nusnet = '%s' AND `date` ='%s'", $_GET['nusnet'], $date);
            $result = $conn->query($querySearch);
            if ($result->num_rows > 0 ) {
                $row = $result->fetch_assoc();
                $diaryID = $row['id'];
            } else {
                $queryInsertDiary = sprintf("INSERT INTO `diary` (`date`,`nusnet`) VALUES ('%s', '%s')", $date, $_GET['nusnet']);
                $conn->query($queryInsertDiary);
                $diaryID = $conn->insert_id;
            }
            $queryInsertTask = sprintf("INSERT INTO `task` (nusnet, id, taskPresent, taskName, module, timeFrom, timeTo, description, week, diaryID)"
                . " VALUES ('%s', '%s', '%s', '%s', '%s' ,'%s', '%s', '%s', '%s', '%s')", $_GET['nusnet'], $_GET['id'], $_GET['taskPresent'] == "true" ? 1 : 0, $_GET['taskName'], $_GET['module'],
                $_GET['timeFrom'], $_GET['timeTo'], $_GET['description'], $_GET['week'], $diaryID);
            $rsInsertTask = mysqli_query($conn, $queryInsertTask);
            if ($rsInsertTask == 1) {
                $message->success = true;
            }
        } 
        echo json_encode($message);
    } catch(ErrorException $e) {
        echo $e->getMessage();
    }

    function computeDate($day, $week) {
        $startDate = 10;
        if ($week < 4) {
           $month = "08"; // Aug
        } else if ($week == 4) {
            if (getDayOffset($day) <= 0) {
                $month = "08";  // Aug
            } else {
                $month = "09"; // Sep
                $startDate = 0;
                $week = 1;
            }
        } else if ($week < 7) {
            $month = "09";  // Sep
            $startDate = 0;
            $week = $week - 3;
        } else if ($week == 7) {
            if (getDayOffset($day) <= 2) {
                $month = "09";
                $startDate = 28;
                $week = 1;
            } else {
                $month = "10";
                $startDate = -2;
                $week = $week - 6;
            }
        } else if ($week <= 11) {
            if (getDayOffset($day) == 6 && $week == 11) {
                $month = "11";
                $week = 0;
                $startDate = 2;
            } else { 
                $month = "10";
                $startDate = 5;
                $week = $week - 7;
            }
        } else {
            $month = "11";
            $startDate = 2;
            $week = $week - 11;
        }
        return "2020-$month-". ($startDate + getDayOffset($day) + (($week - 1) % 4) * 7);
    }

    function getDayOffset($day) {
        $day = strtolower(substr($day, 0, 3));
        if ($day == "mon") {
            return 0;
        } else if ($day == "tue") {
            return 1;
        } else if ($day == "wed") {
            return 2;
        } else if ($day == "thu") {
            return 3;
        } else if ($day == "fri") {
            return 4;
        } else if ($day == "sat") {
            return 5;
        } else if ($day == "sun") {
            return 6;
        }
    }
?>
