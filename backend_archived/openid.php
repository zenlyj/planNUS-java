<?php
    require 'lightopenid.php';
    require_once('Connections/connDB.php');
    
    try {
        # Change 'localhost' to your domain name.
        $openid = new LightOpenID('localhost:80');
        if(!$openid->mode) {
            if (isset($_GET['nusnet'])) {
                $openid->identity = "https://openid.nus.edu.sg/".$_GET['nusnet'];
            }
            elseif (isset($_POST['openid_identifier'])) {
                $openid->identity = $_POST['openid_identifier'];
            }
            # The following two lines request email, full name, and a nickname
            # from the provider. Remove them if you don't need that data.
            $openid->optional = array('contact/email');
            $openid->optional = array('namePerson', 'namePerson/friendly');
            header('Location: ' . $openid->authUrl());
        }
        elseif($openid->mode == 'cancel') {
            echo 'User has canceled authentication!';
        }
        else {
?>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<?php
            $isLoggedIn = $openid->validate();
            $count = 0;
            foreach ($openid->getAttributes() as $key => $value) {
                // $location .= $key . "=" . $value;
                if ($count == 0) {
                    $name = $value;
                    // $location .= "&";
                } else {
                    $nusnet = $value;
                }
                $count++;
            }
            $hash = base64_encode(password_hash(rand() . $nusnet . rand(), PASSWORD_DEFAULT));
            $queryInsertLogin = sprintf("INSERT INTO `user` (nusnet, name, hash)"
			            . " VALUES ('%s', '%s', '%s')", $nusnet, $name, $hash);
            $rsInsertLogin = mysqli_query($conn, $queryInsertLogin);
            $rsID = mysqli_insert_id($conn);
            $queryUpdate = sprintf("UPDATE `user` set hash='%s' where nusnet='%s'", $hash, $nusnet);
            $conn->query($queryUpdate);
        }
    } catch(ErrorException $e) {
        echo $e->getMessage();
    }
?>
<form method=get name="login" action="http://localhost:3000">
    <input type="hidden" name="type" value="login"></input>
    <input type="hidden" name="nusnet" value=<?php echo $nusnet; ?>></input>
    <input type="hidden" name="hash" value=
    <?php 
        echo $hash;
    ?> />
</form>
<script>document.forms['login'].submit();</script>
</body>
</html>
