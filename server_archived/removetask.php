<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # params
    # nusnet:str, id: str (MON1, MON2, THURS4, etc.), taskPresent: true, taskName: str, module: str, timeFrom: str (24hr format), timeTo: str(24hr format), description: str
    # usage http://116.14.246.142/removetask.php?nusnet=e0407306&week=2&id=TUES2
    try {
        $message = new \stdClass();
        $message->success = false;
        if (isset($_GET['nusnet']) && isset($_GET['id'])) {
            $queryDeleteTask = sprintf("DELETE FROM `task` WHERE nusnet='%s' AND id='%s' AND week='%s'", $_GET['nusnet'], $_GET['id'], $_GET['week']);
            $rsDeleteTask = mysqli_query($conn, $queryDeleteTask);
            if ($rsDeleteTask == 1) {
                $message->success = true;
            } 
        } 
        echo json_encode($message);
    } catch(ErrorException $e) {
        echo $e->getMessage();
    }
?>