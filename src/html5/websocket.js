
var App = {
	url: "ws://localhost:1880/",
	channel: "simulator",
	ws: null,

    robots: {},

    charts: [],

    labels: [],
    initialData: [],

    timeSpan: 60,

    Construct: function(){
        for(var i = 0;i < this.timeSpan;i++){
            if(i%4==0){
                this.labels.push(i + 1);
            }else
                this.labels.push("");

            this.initialData.push(0);
        }

        this.CreateChartInstance();
    },

    CreateChartInstance: function(){
        for(var i = 1;i < 5; i++){
            var ctx = document.getElementById("robot" + i).getContext("2d");
            var data = {
                labels : this.labels,
                datasets : [{
                    label: "robot " + i,
                    fillColor: "rgba(151,187,205,0.2)",
                    strokeColor: "rgba(151,187,205,1)",
                    pointColor: "rgba(151,187,205,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: this.initialData
                }]
            };

            var options = {
                bezierCurve : false,
                animation: false
            }

            this.charts["r" + i] = (new Chart(ctx).Line(data, options));
        }
    },

	WebSocketBrowserCheck: function(){
		if (("WebSocket" in window) == false){
			alert("WebSocket is not supported by your Browser!");
			return false;	
		}			
	},
	
	OpenWebSocket: function(){
		this.ws = new WebSocket( this.url + this.channel );
		this.ws.onopen  = this.OnOpen;
		this.ws.onclose = this.OnClose;
		this.ws.onmessage = this.OnMessage;
	},
	
	OnOpen: function(){
		console.log("connected to server");
	},
	
	OnMessage: function(event){
        if(event.data == "" || typeof(event.data) == "undefined")
            return;

        var data = JSON.parse(event.data);

        if(typeof(App.robots[data.robot]) == "undefined")
            App.robots[data.robot] = {
                state: true,
                power: [],
                carId: 0
            };

        switch(data.type){
            case "state":
                App.robots[data.robot].state = data.value;
                break;

            case "power":
                if(data.robot=="r1")
                    console.log(data.value);

                //App.robots[data.robot].power.unshift(parseInt(data.value));
                App.robots[data.robot].power.push(parseInt(data.value));
                App.robots[data.robot].carId = data.carId;

                // clean array so it wont be heavy
                var robot = App.robots[data.robot];
                while(robot.power.length > 60){
                    robot.power.shift();
                }
                break;
        }


        // reupdate the array
        for(var i = App.timeSpan - 1;i >= 0; i--){
            if(App.robots[data.robot].power[i])
                App.charts[data.robot].datasets[0].points[i].value = App.robots[data.robot].power[i];
        }

        // update the chart
        App.charts[data.robot].update();

        // calculate average
        document.getElementById("avg" + data.robot).innerHTML = App.Average(App.robots[data.robot].power);
	},

    Average: function(arr){
        var sum = 0;
        for(var i = 0;i < arr.length;i++){
            sum += arr[i];
        }

        if(arr.length <= 0)
            return 0;

        return sum/arr.length;
    },
	
	OnClose: function(){
		console.log("disconnected to server");		
	},
	
	Run: function(){
		if(this.WebSocketBrowserCheck){
			this.OpenWebSocket();			
		}
	}	
}

/*
 var Robot  {
 state : false,
 power : [],
 carId : 0
 }
 */

/*
 { "robot": "r3", "type": "power", "value": "7", "carId": "_carId", "timestamp": 1415876392505 }
 */