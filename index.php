<?php
include_once "./JavaSide/src/ConsoleDemo/php/ControllerCD.php";

    $timezone = "Asia/Colombo";
    date_default_timezone_set($timezone);
    $today = date("Y-m-d h:m:s");
    
//     $fp = fopen('./Logs/LogX1.txt', 'a');//opens file in append mode
//     fwrite($fp,"---"."\r\n"."AT $today"."\r\n"."Post =".print_r($_POST)."\r\n");
//     fclose($fp); 

    echo "got ".print_r($_POST);
   
    

    $urlParams = $_GET["requestParam"];
    $requests = explode("/", $urlParams);
    $requests = array_map("strtolower", $requests);
    $controller = new ControllerCD();
    if($requests[0] =="connect"){
        $port = 20222;
       echo "<br><br> trying to connect to java server<br>
        <br> port = $port <br>
     
        ";
       clientSocketConnect($port);
    }
    
    
    else if($requests[0]=="api"){
        // link api reference here
        $responceArray = array("responce"=>"RZT");
        $respJson = json_encode($responceArray);
        echo $respJson;
    }
    else if($requests[0]=="consoledemo" && count($requests)==1){
        $controller->displayView();
    }
    else if($requests[0]=="consoledemo" && count($requests)==2 && $requests[1]=="update"){
        $controller->getUpdateNumber();
    }
    else if($requests[0]=="console" && count($requests)==1 ){
        $controller->displayConsoleView();
    }
    else if($requests[0]=="console" && $requests[1]=="externalcommand"  && count($requests)==2){
        ob_end_clean();
        $cmd = $_POST["command"];
        $controller->javaServerConnect($cmd,1111);
    }
    
    
    
   


    function clientSocketConnect($port){
        
        $PORT = $port; //the port on which we are connecting to the "remote" machine
        $HOST = "localhost"; //the ip of the remote machine (in this case it's the same machine)
        
        $sock = socket_create(AF_INET, SOCK_STREAM, 0) //Creating a TCP socket
        or die("error: could not create socket\n");
        
        $succ = socket_connect($sock, $HOST, $PORT) //Connecting to to server using that socket
        or die("error: could not connect to host\n");
        
        $text = "Hello, Java!"; //the text we want to send to the server
        
        socket_write($sock, $text . "\n", strlen($text) + 1) //Writing the text to the socket
        or die("error: failed to write to socket\n");
        
        $reply = socket_read($sock, 10000, PHP_NORMAL_READ) //Reading the reply from socket
        or die("error: failed to read from socket\n");
        
        echo $reply;
        
    }
    
    $_POST = array();
    
    
    
    
    
    
    
    
    
    
    
    
    
?>