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

class Skocko{
	_id;
	_opponentId;
	combo;
	colors;
	isCorrect;
	numOfTries;
	constructor(id, opponentId, combo, colors, isCorrect, numOfTries){
		this._id = id;
		this._opponentId = opponentId;
		this.combo = combo;
		this.colors = colors;
		this.isCorrect = isCorrect;
		this.numOfTries = numOfTries;
	}
}

var playersWaiting = new List([]);
var playersCalculated = [];

io.on('connect', (socket) => {
	console.log("New socket connection: " + socket.id);
	
	socket.on("connect_error", (err) => {
		console.log(`connect_error due to`);
	});

	
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
	if (playersCalculated.filter(e => e._opponentId === playerOneId).length > 0){
			const foundPlayers = playersCalculated.filter(e => e._opponentId === playerOneId);
			
			const playersToRecieve = [];
			playersToRecieve.add(foundPlayers[0]);
			playersToRecieve.add(new MojBroj(playerOneId, playerTwoId, number));
			const index = playersCalculated.indexOf(foundPlayers[0]);
			playersCalculated.splice(index, 1);
			console.log(playersCalculated);
			io.emit('endMojBroj', playersToRecieve);
			return;
		}
		console.log(playerOneId + " " + playerTwoId + " " + number);
		playersCalculated.add(new MojBroj(playerOneId, playerTwoId, number));
		console.log(playersCalculated);
	});
	
	socket.on('sendPlayerSkocko', (playerOneId, playerTwoId, combo, colors, isCorrect, numOfTries) => {
		console.log(isCorrect);
		if (isCorrect){
			console.log(isCorrect)
			io.emit("showPlayerSkockoCorrect", new Skocko(playerOneId, playerTwoId, combo, colors, isCorrect, numOfTries));
			return;
		}
		
		if (numOfTries == 0){
			io.emit("giveOpponentAChanceSkocko", new Skocko(playerOneId, playerTwoId, combo, colors, isCorrect, numOfTries));
		} else {
			io.emit("showPlayerSkocko", new Skocko(playerOneId, playerTwoId, combo, colors, isCorrect, numOfTries));
		}
	});
})