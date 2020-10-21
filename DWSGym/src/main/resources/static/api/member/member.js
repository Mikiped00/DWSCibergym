function addMember() {

	var inputMemberName = document.getElementById('newMemberName').value
	var inputDNI = document.getElementById('newMemberDni').value
	var inputEmail = document.getElementById('newMemberEmail').value
	var inputPass = document.getElementById('newMemberEmail').value
	var memberRol = document.getElementById('newMemberRol').value


	if (inputMemberName == "" || inputDNI == "" || inputEmail == "" || inputPass == "" || memberRol == "") {
		showAlert('Cannot add the member', 'Please fill in all the values.', 'warning', '')
	} else {
		var client = new XMLHttpRequest()
		client.responseType = "json"

		client.addEventListener("load", function () {
			if (this.status == 403) {
				showAlert('Cannot add the member', 'You are not authorized to do this petition.', 'error', '/api/MemberRest.html')
			} else if (this.status == 226) {
				showAlert('Cannot add the member', 'The email is already taken.', 'warning', '/api/MemberRest.html')
			} else {
				showAlert('Member added', 'The member was added successfully', 'success', '/api/MemberRest.html')
			}
		})
		client.open("POST", "https://localhost:8443/CiberGym/api/member/")
		client.setRequestHeader("Content-type", "application/json")
		var add = JSON.stringify({ "dni": inputDNI, "name": inputMemberName, "email": inputEmail })
		client.send(add)
	}

}

//Get all members from the database with REST API
function getMembers() {
	var client = new XMLHttpRequest();
	client.responseType = "json"
	var list = document.getElementById('tableBody')
	list.innerHTML = '';

	client.addEventListener("load", function () {
		if (this.status == 403) {
			showAlert('Cannot get the members', 'You are not authorized to do this petition.', 'error', '/api/MemberRest.html')
		} else {
			var item = client.response;
			for (item of client.response) {
				createChildMemberReduce(item)
			}
		}

	})

	client.open("GET", "https://localhost:8443/CiberGym/api/member/")
	client.send();

}

//Get a specific member from the database with REST API
function getMember() {

	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	const id1 = urlParams.get('id');

	var list = document.getElementById('tableBody')
	list.innerHTML = '';

	var client = new XMLHttpRequest();
	client.responseType = "json"

	client.addEventListener("load", function () {
		if (this.status == 200) {
			var item = client.response
			createChildMemberDetailed(item)
		} else {
			showAlert('Cannot get the member', 'The member introduced is not included in our database.', 'error', '/api/member/getMembersRest.html')
		}

	})

	client.open("GET", "https://localhost:8443/CiberGym/api/member/" + id1)
	client.send();
}

//Modify a specific member to the database with REST API
function modifyMember() {
	var inputMemberName = document.getElementById('updateMemberName').value
	var inputDNI = document.getElementById('updateMemberDni').value

	var client = new XMLHttpRequest()
	client.responseType = "json"

	client.addEventListener("load", function () {
		showAlert('Member modified', 'The member was modified successfully', 'success', '/api/member/showProfile.html')
	})

	client.open("PUT", "https://localhost:8443/CiberGym/api/member/")
	client.setRequestHeader("Content-type", "application/json")
	var add = JSON.stringify({ "dni": inputDNI, "name": inputMemberName })
	client.send(add)
}

//Delete a specific member to the database with REST API
function deleteMember(id) {

	var client = new XMLHttpRequest()
	client.addEventListener("load", function () {
		if (this.status == 403) {
			showAlert('Cannot get the members', 'You are not authorized to do this petition.', 'error', '/api/MemberRest.html')
		} else {
			showAlert('Member deleted', 'The member was deleted successfully', 'success', '/api/member/getMembersRest.html')
		}
	})

	client.open("DELETE", "https://localhost:8443/CiberGym/api/member/" + id)
	client.send();
}


//Add a reservation of a Lesson
function reserveLesson() {
	var select = document.getElementById('selectLessonId')
	var reserveLessonId = select.value;
	var getMember = new XMLHttpRequest();
	getMember.responseType = "json"

	getMember.addEventListener("load", function () {
		if (this.status == 200) {
			showAlert('Succesfully added', 'The member has been registered for the lesson successfully', 'success', '/api/member/showProfile.html')
		} else if (this.status == 429) {
			showAlert('Cannot register the member', 'The member is already registered for a lesson', 'warning', '/api/member/showProfile.html')
		} else if (this.status == 507) {
			showAlert('Cannot register the member', 'There is not any available place for this lesson', 'error', '/api/member/showProfile.html')
		} else if (this.status == 404) {
			showAlert('Cannot register the member', 'The member is not included in our database', 'error', '/api/member/showProfile.html')
		}
	})
	getMember.open("POST", "https://localhost:8443/CiberGym/api/member/reserveLesson/" + reserveLessonId)
	getMember.send();
}

function auxReserveLesson() {
	var select = document.getElementById('selectLessonId')
	var getPetition = new XMLHttpRequest();
	getPetition.responseType = "json";
	getPetition.addEventListener("load", function () {

		for (item of getPetition.response) {
			var option = document.createElement('option')
			option.value = item.id;
			option.text = item.name;
			select.appendChild(option);
		}
	})

	getPetition.open("GET", "https://localhost:8443/CiberGym/api/lesson/")
	getPetition.send();

}

//Delete a reservation of a Lesson
function cancelReservation() {

	var getMember = new XMLHttpRequest();
	getMember.responseType = "json"

	getMember.addEventListener("load", function () {
		var response = new Response();
		if (this.status == 200) {
			showAlert('Succesfully Deleted', 'The member is not registered for this lesson any more', 'success', '')
		} else if (this.status == 204) {
			showAlert('Cannot delete the registration', 'The member is not registered for any lesson', 'warning', '')
		} else if (this.status == 404) {
			showAlert('Cannot delete the registration', 'The member is not included in our database', 'error', '')
		}
	})

	getMember.open("DELETE", "https://localhost:8443/CiberGym/api/member/cancelLesson/")
	getMember.send();

}

function showProfile() {
	var list = document.getElementById('tableBody')
	list.innerHTML = '';

	var client = new XMLHttpRequest();
	client.responseType = "json"

	client.addEventListener("load", function () {
		if (this.status == 200) {
			var item = client.response
			createChildMemberProfile(item)
		} else {
			showAlert('Cannot get the member', 'The member introduced is not included in our database.', 'error', '/api/member/getMembersRest.html')
		}

	})

	client.open("GET", "https://localhost:8443/CiberGym/api/profile/")
	client.send();
}

function changePassword() {
	var password1 = document.getElementById("newPassword").value;
	var password2 = document.getElementById("newPasswordRepeted").value;
	var client = new XMLHttpRequest();
	client.responseType = "json"

	client.addEventListener("load", function () {
		if (this.status == 200) {
			showAlert('Password modified', 'The password was modified successfully', 'success', '/api/member/showProfile.html')
		} else if (this.stauts == 400){
			showAlert('Cannot modify the password', "Check your inputs.", 'warning', '')
		} else {
			showAlert('Cannot modify the password', "Passwords don't match.", 'error', '')
		}

	})

	client.open("PATCH", "https://localhost:8443/CiberGym/api/changePassword?newPass=" + password1 + "&newPassRepeated=" + password2)
	client.send();
}


//Filter by Member's name Beginning
function nameMemberFilter() {
	var name = document.getElementById('name').value
	var results = document.getElementById('tableBody')
	var client = new XMLHttpRequest();
	client.responseType = "json"
	results.innerHTML = '';

	client.addEventListener("load", function () {
		if (this.status == 403) {
			showAlert('Cannot get the members', 'You are not authorized to do this petition.', 'error', '/api/MemberRest.html')
		} else {
			for (item of client.response) {
				createChildMemberDetailed(item)
			}
		}
	})
	client.open("GET", "https://localhost:8443/CiberGym/api/member/filterByBegginingName?name=" + name)
	client.send();
}

//Aux function to show members
function createChildMemberReduce(item) {

	var tbody = document.getElementById('tableBody')
	var tr = document.createElement('tr')

	var itemDni = document.createTextNode(item.dni)
	var dniTd = document.createElement('td');
	dniTd.appendChild(itemDni);

	var itemName = document.createTextNode(item.name)
	var nameTd = document.createElement('td');
	nameTd.appendChild(itemName);

	var itemDetails = document.createElement('input');
	itemDetails.type = 'button';
	itemDetails.id = 'secondInput'
	itemDetails.onclick = function () {
		window.location = "/api/member/getMemberRest.html?id=" + item.id;
	}
	itemDetails.value = "Details";
	var detailsTd = document.createElement('td');
	detailsTd.appendChild(itemDetails);

	var itemDelete = document.createElement('input');
	itemDelete.type = 'button';
	itemDelete.id = 'secondInput'
	itemDelete.onclick = function () {
		deleteMember(item.id)
	}
	itemDelete.value = "Delete";
	var deleteTd = document.createElement('td');
	deleteTd.appendChild(itemDelete);

	tr.appendChild(dniTd);
	tr.appendChild(nameTd);
	tr.appendChild(detailsTd);
	tr.appendChild(deleteTd);
	tbody.appendChild(tr);

}

function createChildMemberDetailed(item) {

	var tbody = document.getElementById('tableBody')
	var tr = document.createElement('tr')

	var itemDni = document.createTextNode(item.dni)
	var dniTd = document.createElement('td');
	dniTd.appendChild(itemDni);

	var itemName = document.createTextNode(item.name)
	var nameTd = document.createElement('td');
	nameTd.appendChild(itemName);

	var itemEmail = document.createTextNode(item.email)
	var emailTd = document.createElement('td');
	emailTd.appendChild(itemEmail);

	var itemFee = document.createTextNode(item.fee)
	var feeTd = document.createElement('td');
	feeTd.appendChild(itemFee);

	tr.appendChild(dniTd);
	tr.appendChild(nameTd);
	tr.appendChild(emailTd);
	tr.appendChild(feeTd);

	tbody.appendChild(tr);
}

//Aux function to show the profile of a member
function createChildMemberProfile(item) {

	var tbody = document.getElementById('tableBody')
	var tr = document.createElement('tr')

	var itemDni = document.createTextNode(item.dni)
	var dniTd = document.createElement('td');
	dniTd.appendChild(itemDni);

	var itemName = document.createTextNode(item.name)
	var nameTd = document.createElement('td');
	nameTd.appendChild(itemName);

	var itemEmail = document.createTextNode(item.email)
	var emailTd = document.createElement('td');
	emailTd.appendChild(itemEmail);

	var itemFee = document.createTextNode(item.fee)
	var feeTd = document.createElement('td');
	feeTd.appendChild(itemFee);

	var itemReservation = document.createTextNode(item.membersLesson)
	var reservationTd = document.createElement('td');
	reservationTd.appendChild(itemReservation);

	var itemModify = document.createElement('input');
	itemModify.type = 'button';
	itemModify.id = 'secondInput'
	itemModify.onclick = function () {
		window.location = "/api/member/putMemberRest.html";
	}
	itemModify.value = "Modify";
	var modifyTd = document.createElement('td');
	modifyTd.appendChild(itemModify);

	var itemChangePassword = document.createElement('input');
	itemChangePassword.type = 'button';
	itemChangePassword.id = 'secondInput'
	itemChangePassword.onclick = function () {
		window.location = "/api/member/changePasswordRest.html";
	}
	itemChangePassword.value = "Change password";
	var changePassTd = document.createElement('td');
	changePassTd.appendChild(itemChangePassword);

	var itemReserve = document.createElement('input');
	itemReserve.type = 'button';
	itemReserve.id = 'secondInput'
	itemReserve.onclick = function () {
		window.location = "/api/member/reserveLessonRest.html";
	}
	itemReserve.value = "Reserve";
	var reserveTd = document.createElement('td');
	reserveTd.appendChild(itemReserve);

	var itemCancel = document.createElement('input');
	itemCancel.type = 'button';
	itemCancel.id = 'secondInput'
	itemCancel.onclick = function () {
		cancelReservation();
	}
	itemCancel.value = "Cancel";
	var cancelTd = document.createElement('td');
	cancelTd.appendChild(itemCancel);

	tr.appendChild(dniTd);
	tr.appendChild(nameTd);
	tr.appendChild(emailTd);
	tr.appendChild(feeTd);
	tr.appendChild(reservationTd);
	tr.appendChild(modifyTd);
	tr.appendChild(changePassTd);
	tr.appendChild(reserveTd);
	tr.appendChild(cancelTd);
	tbody.appendChild(tr);

}

function showAlert(title, text, icon, path) {
	Swal.fire({
		title: title,
		text: text,
		icon: icon,
		confirmButtonText: 'Go back'
	}).then((result) => {
		if (result.value) {
			window.location.href = path
		}
	})
}

