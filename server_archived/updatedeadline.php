<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # params
    # usage http://localhost/updatedeadline.php?nusnet=e0407306&id=16-09-2020test&fields=deadlineName|xyz,description|GGGFSGSG
    try {
        $message = new \stdClass();
        $message->success = false;
        if (isset($_GET['nusnet']) && isset($_GET['id'])) {
            $queryUpdateDeadline = "UPDATE deadline SET ";
            $count = 0;
            $fields = explode(",", $_GET['fields']);
            foreach ($fields as $field) {
                $name = explode("|", $field)[0];
                $value = explode("|", $field)[1];
                if ($name == "deadline") {
                    $value = explode("-", $value);
                    $value = $value[2].'-'.$value[1].'-'.$value[0];
                }
                if ($count == 0) {;
                } else {
                    $queryUpdateDeadline = $queryUpdateDeadline . ",";
                }
                $queryUpdateDeadline = $queryUpdateDeadline . sprintf("%s='%s'", $name, $value);
                $count++;
            }
            $queryUpdateDeadline = $queryUpdateDeadline . sprintf(" WHERE nusnet='%s' AND id='%s'", $_GET['nusnet'], $_GET['id']);
            $rsUpdateDeadline = mysqli_query($conn, $queryUpdateDeadline);
            if ($rsUpdateDeadline == 1) {
                $message->success = true;
            } 
        } 
        echo json_encode($message);
    } catch(ErrorException $e) {
        echo $e->getMessage();
    }
?>
