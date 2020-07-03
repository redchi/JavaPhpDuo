<?php
Class ControllerCD{
    
    public function __construct(){
       
    }
    
    public function getUpdateNumber(){
       
        ob_end_clean();
        
        //echo date('Y-m-d H:i:s', time());
       echo $this->clientSocketConnect(20222);
        
      //  echo "done";
    }
    
   private function clientSocketConnect($port){
        
        $PORT = $port; //the port on which we are connecting to the "remote" machine
        $HOST = "localhost"; //the ip of the remote machine (in this case it's the same machine)
        
        $sock = socket_create(AF_INET, SOCK_STREAM, 0) //Creating a TCP socket
        or die("error: could not create socket\n");
        
        $succ = socket_connect($sock, $HOST, $PORT) //Connecting to to server using that socket
        or die("error: could not connect to host\n");
        
        $text = "give update"; //the text we want to send to the server
        
        socket_write($sock, $text . "\n", strlen($text) + 1) //Writing the text to the socket
        or die("error: failed to write to socket\n");
        
        $reply = socket_read($sock, 10000, PHP_NORMAL_READ) //Reading the reply from socket
        or die("error: failed to read from socket\n");
        echo "<br>";
        echo $reply;
        
    }
    
    public function javaServerConnect($cmd,$port){
//         $PORT = 51396; //the port on which we are connecting to the "remote" machine
//         $HOST = "89.242.50.152"; //the ip of the remote machine (in this case it's the same machine)
        
        $PORT = 1289; //the port on which we are connecting to the "remote" machine
        $HOST = "192.168.1.19"; //the ip of the remote machine (in this case it's the same machine)
        
        $sock = socket_create(AF_INET, SOCK_STREAM, 0) //Creating a TCP socket
        or die("error: could not create socket\n");
        
        $succ = socket_connect($sock, $HOST, $PORT) //Connecting to to server using that socket
        or die("error: could not connect to host\n");
      //  echo "sent ->".$cmd."<-\n";
        $text = $cmd; //the text we want to send to the server
        
        socket_write($sock, $text . "\n", strlen($text) + 1) //Writing the text to the socket
        or die("error: failed to write to socket\n");
        
        $reply = socket_read($sock, 10000, PHP_NORMAL_READ) //Reading the reply from socket
        or die("error: failed to read from socket\n");
        $output = $this->processJsonForConsole($reply);
        echo $output;
        //echo "\nyeet";
    }
    
    private function processJsonForConsole($json){
        $jsonArr = json_decode($json);
        $responceString = $jsonArr->responce;
        
        $linebreakAll = $jsonArr->linebreaks;
    //    echo "#1";
      //  echo "\n".$linebreakAll."\n";
     
        if(empty($linebreakAll)==false){
            $linebreaks = explode(",", $linebreakAll);
           // echo "#2";
          //  $count = 0;
            foreach ($linebreaks as $linebreakPos) {
           //     echo "got - ". $linebreakPos;
                if(empty($linebreakPos)==false){
                    // add line break
                    $pos = (int)$linebreakPos;
                  //  $pos = $pos - $count;
           //         echo "replaced pos ".$linebreakPos;
                    $responceString = substr_replace($responceString, "\n", $pos, 0);
                    //$count = $count + 1;
                }
              
            }
            
        }
      //  echo "#3";
        return $responceString;
    }
    
    
    public function displayView(){
        echo 
        '
        <!DOCTYPE html>
        <html  >
        <head>
          
        </head>
        
        <script>
        	setInterval(update, 2000);
        
        	function update(){
        		var request = new XMLHttpRequest();
        		request.open("GET","http://localhost/consoleDemo/update")
        		request.onload = function(){
        			var replyedData = request.responseText;
        			document.getElementById("info").innerHTML = replyedData;
        		};
        		request.send();
        	}
        
        
        </script>
        	<body>
        	  
        		<h1>demo view</h1>
        		<br>
        		<div id = "info">
                    yee test
        		</div>
        	</body>
        </html>
        ';
    }
    
    public function displayConsoleView(){#
        ob_end_clean();
        
        echo
        '


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="apple-touch-icon" type="image/png" href="https://static.codepen.io/assets/favicon/apple-touch-icon-5ae1a0698dcc2402e9712f7d01ed509a57814f994c660df9f7a952f3060705ee.png" />
<meta name="apple-mobile-web-app-title" content="CodePen">
<link rel="shortcut icon" type="image/x-icon" href="https://static.codepen.io/assets/favicon/favicon-aec34940fbc1a6e787974dcd360f2c6b63348d4b1f4e06c77743096d55480f33.ico" />
<link rel="mask-icon" type="" href="https://static.codepen.io/assets/favicon/logo-pin-8f3771b1072e3c38bd662872f6b673a722f4b3ca2421637d5596661b4e2132cc.svg" color="#111" />
<title>CodePen - A Pen by  redchi</title>
<style>
html,
body {
  min-height: 100%;
}
h2{
  color:#fffcfc;
   padding-left: 0.5em;
}
body {
  background-color: #525252;
  

  /*padding: 0.5em 1em;*/
  -webkit-font-smoothing: antialiased;
}

.console {
  position: fixed;
  font-family: monospace,monospace;
  color: #fff;
  width: calc(100% - 3em);
  max-width: 100%;
  max-height: calc(100% - 3em);
  overflow-y: auto;
  margin: 1em 1em;
  padding-top: 0.5em;
  padding-bottom: 0.5em;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 0.5em;
  box-shadow: 0 0.75em 3em rgba(50, 50, 50, 0.5);
  z-index: 100;
  line-height: 1.5;
}

.console-input {
  font-family: monospace,monospace;
  background-color: transparent;
  border: none;
  outline: none;
  color: #fff;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  min-width: calc(100% - 2em);
  resize: none;
}

.console-input:before {
  content: \'> \';
  color: #FC3565;
  vertical-align: top;
}

.output-cmd:before {
  content: \'> \';
  color: #FC3565;
  vertical-align: top;
}
.output-text:before {
  content: \'> \';
  color: #5F8787;
  font-weight: 600 !important;
  vertical-align: top;
}

.output-text,
.output-cmd {
  width: 100%;
  display: block;
}

.console-inner {
  padding: 0.3em 1.1em;
}

.output-text,
.output-cmd {
  display: block;
  white-space: pre;
}

#outputs div {
  opacity: 0.85;
}

#outputs div div {
  color: #46f01d;
  opacity: 0.8;
  text-decoration: none;
}

#outputs a {
  color: #46f01d;
  opacity: 0.8;
  text-decoration: underline;
}

.console-inner .output-text#ready {
  color: #3df5b8;
  font-style: italic;
  opacity: 0.75;
}

.particles-js-canvas-el {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}
</style>
</head>
<body translate="no">
<body id="particles-js">
<h2> External java console</h2>
<div class=\'console\'>
<div class=\'console-inner\'>
<div id="outputs">
</div>
<div class=\'output-cmd\'><textarea autofocus class=\'console-input\' placeholder="Type command..."></textarea></div>
</div>
</div>
</body>
<script src=\'https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.2/jquery.min.js\'></script>
<script src=\'https://cdnjs.cloudflare.com/ajax/libs/markdown-it/8.1.0/markdown-it.min.js\'></script>
<script src=\'https://cdnjs.cloudflare.com/ajax/libs/autosize.js/3.0.18/autosize.min.js\'></script>
<script src=\'https://unpkg.com/@wmhilton/cli-parser@1.1.0\'></script>
<script src=\'https://s.codepen.io/assets/libs/modernizr.js\'></script>
<script src=\'https://cdnjs.cloudflare.com/ajax/libs/particles.js/2.0.0/particles.min.js\'></script>
<script id="rendered-js">
// Output Welcome message
//output(\'Original console design by Matt Cowley https://codepen.io/MattCowley/pen/jqBbdG/\')
//output(\'\')
output(\'Welcome to example console program.\')

// User Commands
function echo (...a) {
  return a.join(\' \')
}
echo.usage = "echo arg [arg ...]"
echo.doc = "Echos to output whatever arguments are input"

var cmds = {
  clear,
  
}

/*
 * * * * * * * * USER INTERFACE * * * * * * *
 */

function clear () {
  $("#outputs").html("")
}
clear.usage = "clear"
clear.doc = "Clears the terminal screen"

function help (cmd) {
  if (cmd) {
    let result = ""
    let usage = cmds[cmd].usage
    let doc = cmds[cmd].doc
    result += (typeof usage === \'function\') ? usage() : usage
    result += "\n"
    result += (typeof doc === \'function\') ? doc() : doc
    return result
  } else {
    let result = "**Commands:**\n\n"
    print = Object.keys(cmds)
    for (let p of print) {
      result += "- " + p + "\n"
    }
    return result
  }
}
help.usage = () => "help [command]"
help.doc = () => "Without an argument, lists available commands. If used with an argument displays the usage & docs for the command."

// Set Focus to Input
$(\'.console\').click(function() {
  $(\'.console-input\').focus()
})

// Display input to Console
function input() {
  var cmd = $(\'.console-input\').val()
  $("#outputs").append("<div class=\'output-cmd\'>" + cmd + "</div>")
  $(\'.console-input\').val("")
  autosize.update($(\'textarea\'))
  $("html, body").animate({
    scrollTop: $(document).height()
  }, 300);
  return cmd
}

// Output to Console
function output(print) {
  if (!window.md) {
    window.md = window.markdownit({
      linkify: true,
      breaks: true
    })
  }
  $("#outputs").append(window.md.render(print))  
  $(".console").scrollTop($(\'.console-inner\').height());
}

// Break Value
var newLine = "<br/> &nbsp;";

autosize($(\'textarea\'))

var cmdHistory = []
var cursor = -1

// Get User Command
$(\'.console-input\').on(\'keydown\', function(event) {
  if (event.which === 38) {
    // Up Arrow
    cursor = Math.min(++cursor, cmdHistory.length - 1)
    $(\'.console-input\').val(cmdHistory[cursor])
  } else if (event.which === 40) {
    // Down Arrow
    cursor = Math.max(--cursor, -1)
    if (cursor === -1) {
      $(\'.console-input\').val(\'\')
    } else {
      $(\'.console-input\').val(cmdHistory[cursor])
    }
  } else if (event.which === 13) {
    event.preventDefault();
    cursor = -1
    let text = input()
    let args = getTokens(text)[0]
    let cmd = args.shift().value
    args = args.filter(x => x.type !== \'whitespace\').map(x => x.value)
    cmdHistory.unshift(text)
    if (typeof cmds[cmd] === \'function\') {
      let result = cmds[cmd](...args)
      if (result === void(0)) {
        // output nothing
      } else if (result instanceof Promise) {
        result.then(output)
      } else {
        console.log(result)
        output(result)
      }
    } else {
       processCommand(text);
    }
  }
});

function processCommand(cmd){
   sendCommand(cmd);
}

	function sendCommand(cmd){
		var request = new XMLHttpRequest();
		request.open("POST","http://localhost/console/ExternalCommand",true)
		request.onload = function(){
			var replyedData = request.responseText;
			output(replyedData);
		};
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		var params = \'command=\'+cmd;
		request.send(params);
	}

    </script>
</body>
</html>

';
    }
    
    
}

?>