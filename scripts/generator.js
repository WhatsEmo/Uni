var Firebase = require('firebase');

var firebase = new Firebase('https://uni-ios-scratch.firebaseio.com/');
var users = firebase.child("users");

// courses and interests are JSON objects
function createUser(courses, email, interests, name, sid) {
	var uid = users.push({
		courses: courses,
		email: email,
		interests: interests,
		name: name,
		sid: sid
	}).key();
	console.log("Created user with UID: " + uid);
}

function generateUsers(numUsers) {
	for (var i = 0; i < numUsers; ++i) {
		createUser(courses, "u" + i + "@uci.edu", interests, "user " + i, "uci");
	}
}

// creating dummy classes/interests
var courses = {
	111: "class one",
	333: "class three",
	999: "class nine"
};

var interests = {
	0: "ball",
	1: "is",
	2: "lyfe"
};

// adding 5 users
generateUsers(5);
