<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # usage http://116.14.246.142/verifylogin.php?nusnet=xxxxx&hash=xxxxxx...
    try {
        $message = new \stdClass();
        $message->success = false;
        if (isset($_GET['nusnet']) && isset($_GET['hash'])) {
            $nusnet = $_GET['nusnet'];
            $hash = $_GET['hash'];
            $message->nusnet = $nusnet;
            $querySearch = sprintf("SELECT * from `user` where nusnet = '%s'", $_GET['nusnet']);
            $result = $conn->query($querySearch);
            if ($result->num_rows > 0 ) {
                if ($result->fetch_assoc()['hash'] == $hash && $hash != "") {
                    $message->success = true;
                }
                
                $queryUpdate = sprintf("UPDATE `user` set hash='%s' where nusnet='%s'", '', $nusnet);
                $conn->query($queryUpdate);
            } 
        } 
        echo json_encode($message);
    } catch(ErrorException $e) {
        echo $e->getMessage();
    }
?>
