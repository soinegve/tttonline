var ws;
var current_player;
var other_player;
function connect() {
    var username = document.getElementById("username").value;
    
    var host = document.location.host;
    var pathname = document.location.pathname;
    
    ws = new WebSocket("ws://" +host   + "/tictactoe/" + username);

    ws.onmessage = function(event) {

        var message = JSON.parse(event.data);
        var ct = message.content
        console.log(ct)
        var comps = ct.split("#")
        var firstComp = comps[0]
        if (firstComp.startsWith("NextMove"))
        {
            set_button(comps[1]+"_"+comps[2],other_player)
            disableButtons(false)
        }
        else if(firstComp.startsWith("YouAre"))
        {
            var comps = ct.split("#")
            current_player = comps[1]
            other_player = comps[2]
        }
        else if(firstComp.startsWith("GameOver"))
        {
            alert(ct)
        }

    };
}

function send(content) {

    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);

    disableButtons(true)
}


function click_function(input)
{
    set_button(input,current_player)
    send("NextMove#"+input)

}

function disableButtons(yes)
{
    all_buttos = document.getElementsByTagName("button")

    for(var i=1; i <=9 ; i++)
    {
        if (all_buttos[i].textContent == "_")
            all_buttos[i].disabled = yes

    }

}


function set_button(inp_id, val_to_set)
{
    b = document.getElementById(inp_id)
    b.textContent = val_to_set
    b.disabled=true;

}