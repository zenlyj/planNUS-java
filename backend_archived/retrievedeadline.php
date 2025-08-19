<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # usage http://116.14.246.142/retrievedeadline.php?nusnet=e0407306
    try {
        if (isset($_GET['nusnet'])) {
            $querySearch = sprintf("SELECT * from `deadline` where nusnet = '%s'", $_GET['nusnet']);
            $result = $conn->query($querySearch);
            if ($result->num_rows > 0 ) {
                $resultArr = array();
                while ($row = $result->fetch_assoc()) {
                    $date = $row['deadline'];
                    $date = explode('-', $date);
                    if ($date[0] == "0000") {
                        $date = "";
                    } else {
                        $date = $date[2] ."-" . $date[1] . "-" .$date[0];
                    }
                    $row['deadline'] = $date;
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

