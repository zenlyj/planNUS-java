<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # usage http://116.14.246.142/retrievediary.php?nusnet=e0407306
    try {
        $message = new \stdClass();
        if (isset($_GET['nusnet'])) {
            $querySearch = sprintf("SELECT task.id as id, diary.date as `date`, task.completed as `completed` FROM `task` INNER JOIN `diary` ON task.diaryID = diary.id where task.nusnet = '%s'", $_GET['nusnet']);
            $result = $conn->query($querySearch);
            if ($result->num_rows > 0 ) {
                $resultArr = array();
                while ($row = $result->fetch_assoc()) {
                    $date = explode("-", $row['date']);
                    $date = $date[2] . '-' . $date[1] . '-' . $date[0];
                    $row['date'] = $date;
                    $row['completed'] = $row['completed'] == 1 ? true : false;
                    $resultArr[] = $row;
                }
                $message->tasks = $resultArr;
            }

            $querySearch = sprintf("SELECT DISTINCT task.week FROM `task` INNER JOIN `diary` ON task.diaryID = diary.id where task.nusnet = '%s'", $_GET['nusnet']);
            $result = $conn->query($querySearch);
            if ($result->num_rows > 0 ) {
                $resultArr = array();
                while ($row = $result->fetch_assoc()) {
                    $resultArr[] = $row['week'];
                }
                $message->weeks = $resultArr;
            }

            $querySearch = sprintf("SELECT DISTINCT diary.date, task.week, diary.note FROM `task` INNER JOIN `diary` ON task.diaryID = diary.id where task.nusnet = '%s'", $_GET['nusnet']);
            $result = $conn->query($querySearch);
            if ($result->num_rows > 0 ) {
                $resultArr = array();
                while ($row = $result->fetch_assoc()) {
                    $date = explode("-", $row['date']);
                    $dateWeek = new \stdClass();
                    $dateWeek->date = $date[2] . '-' . $date[1] . '-' . $date[0];
                    $dateWeek->week = $row['week'];
                    $dateWeek->note = $row['note'];
                    $resultArr[] = $dateWeek;
                }
                $message->dates = $resultArr;
            }
            echo json_encode($message);
        } else {
            echo "failed";
        }
    } catch(ErrorException $e) {
        echo "failed";
        echo $e->getMessage();
    }
?>

