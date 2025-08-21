<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # usage http://116.14.246.142/retrievedistinctmodule.php?nusnet=e0407306&week=1
    try {
        if (isset($_GET['nusnet'])) {
            $querySearch = sprintf("SELECT DISTINCT(module) FROM `task` WHERE nusnet='%s' AND week='%s'", $_GET['nusnet'], $_GET['week']);
            $result = $conn->query($querySearch);
            if ($result->num_rows > 0 ) {
                $resultArr = array();
                while ($row = $result->fetch_assoc()) {
                    $resultArr[] = $row;
                }
                echo json_encode($resultArr);
            }
        } else {
            echo "failed";
        }
    } catch(ErrorException $e) {
        echo "failed";
        echo $e->getMessage();
    }
?>

