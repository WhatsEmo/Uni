var Firebase = require('firebase');

var firebase = new Firebase('https://uni-database.firebaseio.com/');
var users = firebase.child("users");

// courses and interests are JSON objects
function createUser(courses, email, interests, name, sid) {
	firebase.createUser({
		email: email,
		password: email
	}, function(err, userData) {
		if (err) {
			console.log(err);
		}
		else {
			var newUser = {
					courses: courses,
					email: email,
					interests: interests,
					name: name,
					sid: sid
				}
			users.child(userData.uid).set(newUser);
			for (course in courses) {
				firebase.child('schools').child('uci').child('courses').child(course).child('roster').child(userData.uid).set(name);
			}
			for (interest in interests) {
				firebase.child('schools').child('uci').child('interests').child(interests[interest]).child(userData.uid).set(name);
			}
			console.log('Created user with uid: ' + userData.uid);
		}
	})
}

function generateUsers(numUsers) {
	for (var i = 0; i < numUsers; ++i) {
		var func = createUser(courses, "a" + i + "@uci.edu", interests, "user a" + i, "uci");
	}
	firebase.unauth();
}

// creating dummy classes/interests
var courses = {
	111: "dicks",
	333: "ICS 53",
	555: "10101"
};

var interests = {
	0: "ball",
	1: "is",
	2: "lyfe"
};

// adding 5 users
firebase.authWithCustomToken('tYtVLWNEIHG7elPsrzBRs0taV9NBCwcknek6Bp7y', generateUsers(5));
