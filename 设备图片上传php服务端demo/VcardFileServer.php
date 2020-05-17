<?php

    error_reporting(E_ALL);
    // error_reporting = E_ALL & ~E_NOTICE
    ini_set('display_errors', '1');

    $Time = str_replace("'", "", $_REQUEST["time"]);//Time
    $Deviceid = str_replace("'", "", $_REQUEST["sn"]);//sn
    $C = str_replace("'", "", $_REQUEST["C"]);//C
    date_default_timezone_set("PRC");
    error_log( "Now Time:".  date("Y-m-d H:i:s",time()));
    error_log( "Record Time:".  date("Y-m-d H:i:s",time($Time)));
    error_log("Deviceid:".$Deviceid);
    error_log("Record Temp:".($C/100) ."℃" );

    //判断是否有文件上传
    if (isset($_FILES['image'])) {
        $target_path  = "capture/" . $_FILES['image']['name'];
        error_log("path: " .  $target_path . " tmp:" . $_FILES['image']['tmp_name']); 
        error_log($_FILES['image']['type']); 
        //将文件从临时目录拷贝到指定目录
        if(move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {    
            error_log("The file ".basename( $_FILES['image']['name']). " has been uploaded");
        }  else{    
            error_log("There was an error uploading the file, please try again!  " . $_FILES['image']['error']);
        }
        exit;
    } else  {
        error_log("没有文件");
    }
?>