<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # usage http://116.14.246.142/calculateworkload.php?nusnet=e0407306&week=1&modules=CS1010,CS1231,etc..
    try {
        $totalWorkload = new \stdClass();
        $totalWorkload->hours = 0;
        $existingWorkHours = 0;
        if (isset($_GET['modules']) && $_GET['modules'] != "") {
            $modules = explode(',', $_GET['modules']);
            $baseURL = "https://api.nusmods.com/v2/2019-2020/modules/";
            $totalWorkload->module_hours = array();
            foreach ($modules as $module) {
                $querySearch = sprintf("SELECT * from `task` where nusnet = '%s' and week = '%s' and module = '%s'", $_GET['nusnet'], $_GET['week'], $module);
                $result = $conn->query($querySearch);
                if ($result->num_rows > 0 ) {
                    $existingWorkHours = 0;
                    while ($row = $result->fetch_assoc()) {
                        $existingWorkHours += ($row['timeTo'] - $row['timeFrom']) / 100;
                    }
                }
                $fetchURL = $baseURL . $module . ".json";
                $file_headers = @get_headers($fetchURL);
                if ($file_headers[0] == 'HTTP/1.1 404 Not Found') {
                    $obj = new \stdclass();
                    $obj->moduleCredit = 0;
                } else {
                    $json = file_get_contents($fetchURL);
                    $obj = json_decode($json);
                }
                $totalWorkload->hours += $obj->moduleCredit * 2.5 - ($existingWorkHours >= $obj->moduleCredit * 2.5 ? $obj->moduleCredit * 2.5 : $existingWorkHours);
                $totalWorkload->module_hours[$module] = $obj->moduleCredit * 2.5;
            }
            echo json_encode($totalWorkload);
        } else {
            echo json_encode($totalWorkload);
        }
    } catch(ErrorException $e) {
        echo "failed";
        echo $e->getMessage();
    }
?>
