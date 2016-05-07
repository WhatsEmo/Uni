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
			firebase.authWithPassword({
				email: email,
				password: email
			}, function(err, authData) {
				if(!err) {
					var onComplete = function(err) {
						firebase.unauth();
					}
					users.child(userData.uid).set(newUser, onComplete);
					console.log('Created user with UID: ' + userData.uid);
				}
			})
		}
	})
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
generateUsers(3);
