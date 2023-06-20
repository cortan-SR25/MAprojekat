const express = require('express');
const socket = require('socket.io');
const fs = require('fs');
const app = express();
const port = 3000;

const server = app.listen(port);
app.use(express.static('public'));
console.log('Server is running');
const io = socket(server);
var count = 0;

var List = require("collections/list");

class MojBroj{
	_id;
	_opponentId;
	number;
	constructor(id, opponentId, number){
		this._id = id;
		this._opponentId = opponentId;
		this.number = number;
	}
}

var playersWaiting = new List([]);
var playersCalculated = [];

io.on('connection', (socket) => {
	console.log("New socket connection: " + socket.id);
	
	socket.on('playerReady', (player) => {
		playersWaiting.add(player);
		console.log(player);
		if (playersWaiting.length == 2){
			io.emit('startGame', playersWaiting.toJSON());
			console.log(playersWaiting);
			playersWaiting = new List([]);
		}
	});
	
	socket.on('playerCalculatedNumber', (playerOneId, playerTwoId, number) => {
		if (playersCalculated.filter(e => e._opponentId === '1').length > 0){
			const foundPlayers = playersCalculated.filter(e => e._opponentId === '1');
			
			//promeniti da ne budu zakucane vrednosti vec pravi ID-jevi igraca
			const index = playersCalculated.indexOf(foundPlayers[0]);
			playersCalculated.splice(index, 1);
			console.log(playersCalculated);
			socket.emit('endMojBroj', new MojBroj(playerOneId, playerTwoId, number)); //ovo 1 ce biti playerID jednog od dva igraca
			return;
		}
		console.log(playerOneId + " " + playerTwoId + " " + number);
		playersCalculated.add(new MojBroj(playerOneId, playerTwoId, number));
		console.log(playersCalculated);
	});
})