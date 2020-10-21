//Add Lesson to the database with REST API
function addLesson() {
    var inputLessonName = document.getElementById('newLessonName').value
    var inputLessonDescription = editor.getData();
    var inputAvailablePlaces = document.getElementById('newLessonAvailablePlaces').value
    var inputBegginingHour = document.getElementById('newBegginingHour').value
    var inputBegginingMinutes = document.getElementById('newBegginingMinutes').value
    var inputEndingHour = document.getElementById('newEndingHour').value
    var inputEndingMinutes = document.getElementById('newEndingMinutes').value

    if (inputAvailablePlaces == "" || inputBegginingHour == "" || inputBegginingMinutes == "" ||
        inputEndingHour == "" || inputEndingMinutes == "" || inputLessonDescription == "" || inputLessonName == "") {
        showAlert('Cannot add the activity', 'Please fill in all the values.', 'warning', '')
    } else {
        var client = new XMLHttpRequest()
        client.responseType = "json"

        client.addEventListener("load", function () {
            if (this.status == 201) {
                showAlert('Activity added', 'The activity was added successfully', 'success', '/api/LessonRest.html')
            } else if (this.status == 406) {
                showAlert('Cannot add the lesson', 'You are trying a XSS attack. Behave properly.', 'error', '')
            } else if (this.status == 403) {
                showAlert('Cannot add the lesson', 'You are not authorized to do this petition.', 'error', '/api/LessonRest.html')
            }
        })

        client.open("POST", "https://localhost:8443/CiberGym/api/lesson/")
        client.setRequestHeader("Content-type", "application/json")
        var add = JSON.stringify({ "name": inputLessonName, "description": inputLessonDescription, "availablePlaces": inputAvailablePlaces, "begginingHour": inputBegginingHour, "begginingMinutes": inputBegginingMinutes, "endingHour": inputEndingHour, "endingMinutes": inputEndingMinutes })
        client.send(add)
    }


}

//Get all Lessons from the database with REST API
function getLessons() {
    var client = new XMLHttpRequest();
    client.responseType = "json"
    var results = document.getElementById('tableBody')
    results.innerHTML = '';

    client.addEventListener("load", function () {
        if (this.status == 202) {
            document.getElementById("modify").remove();
            document.getElementById("delete").remove();
        }
        for (item of this.response) {
            createChildLessonReduce(item, this.status)
        }
    })

    client.open("GET", "https://localhost:8443/CiberGym/api/lesson/")
    client.send();

}

//Get a specific Lesson from the database with REST API
function getLesson() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id1 = urlParams.get('id');

    var client = new XMLHttpRequest();
    client.responseType = "json"
    var results = document.getElementById('tableBody')
    results.innerHTML = '';

    client.addEventListener("load", function () {
        if (this.status == 200) {
            var item = client.response
            createChildLessonComplete(item)
        } else {
            showAlert('Cannot get the lesson', 'The lesson introduced is not included in our database.', 'error', '/api/lesson/getLessonsRest.html')
        }
    })

    client.open("GET", "https://localhost:8443/CiberGym/api/lesson/" + id1)
    client.send();
}

//Modify a specific Lesson from the database with REST API
function modifyLesson() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id1 = urlParams.get('id');
    var inputModifyLessonName = document.getElementById('updateLessonName').value
    var inputLessonDescription = editor.getData();
    var inputAvailablePlaces = document.getElementById('availablePlaces').value
    var inputBegginingHour = document.getElementById('updateBegginingHour').value
    var inputBegginingMinutes = document.getElementById('updateBegginingMinutes').value
    var inputEndingHour = document.getElementById('updateEndingHour').value
    var inputEndingMinutes = document.getElementById('updateEndingMinutes').value

    var client = new XMLHttpRequest()
    client.responseType = "json"

    client.addEventListener("load", function () {
        if (this.status == 200) {
            showAlert('Activity modified', 'The activity was modified successfully', 'success', '/api/lesson/getLessonsRest.html')
        } else if (this.status == 406) {
            showAlert('Cannot modify the lesson', 'You are trying a XSS attack. Behave properly.', 'error', '')
        } else if (this.status == 403) {
            showAlert('Cannot modify the lesson', 'You are not authorized to do this petition.', 'error', '/menuAPI')
        }
    })

    client.open("PUT", "https://localhost:8443/CiberGym/api/lesson/" + id1)
    client.setRequestHeader("Content-type", "application/json")
    var add = JSON.stringify({ "name": inputModifyLessonName, "description": inputLessonDescription, "availablePlaces": inputAvailablePlaces, "begginingHour": inputBegginingHour, "begginingMinutes": inputBegginingMinutes, "endingHour": inputEndingHour, "endingMinutes": inputEndingMinutes })
    client.send(add)
}

//Delete a specific Lesson from the database with REST API
function deleteLesson(id) {
    var client = new XMLHttpRequest();
    client.responseType = "json"
    client.addEventListener("load", function () {
        if (this.status == 403) {
            showAlert('Cannot modify the lesson', 'You are not authorized to do this petition.', 'error', '/api/LessonRest.html')
        } else {
            showAlert('Activity deleted', 'The activity was deleted successfully', 'success', '')
        }
    })

    client.open("DELETE", "https://localhost:8443/CiberGym/api/lesson/" + id)
    client.send();
}

//Get the members with a reservation for a Lesson
function getMemberByLesson() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const inputGetLessonID = urlParams.get('id');
    var client = new XMLHttpRequest();
    client.responseType = "json"

    client.addEventListener("load", function () {
        if (this.status == 200) {
            for (item of client.response) {
                createChildMemberComplete(item)
            }
        } else if (this.status == 204) {
            showAlert('Cannot get the members', 'There are no members registered for this lesson.', 'warning', '/api/lesson/getLessonsRest.html')
        } else if (this.status == 404) {
            showAlert('Cannot get the members', 'The lesson introduced is not included in our database.', 'error', '/api/lesson/getLessonsRest.html')
        }

    })

    client.open("GET", "https://localhost:8443/CiberGym/api/lesson/" + inputGetLessonID + "/getReservedMember/")
    client.send();
}

function goBack() {
    window.history.back();
}

//FILTERS 


function lessonFilters() {

    var name = document.getElementById('name').value
    var comparePlacesOver = document.getElementById('placesOver').value
    var comparePlacesUnder = document.getElementById('placesUnder').value
    var comparebegHourBefore = document.getElementById('begHourBefore').value
    var comparebegHourAfter = document.getElementById('begHourAfter').value
    var compareendHourBefore = document.getElementById('endHourBefore').value
    var compareendHourAfter = document.getElementById('endHourAfter').value

    var results = document.getElementById('tableBody')
    var client = new XMLHttpRequest();
    client.responseType = "json"
    results.innerHTML = '';

    client.addEventListener("load", function () {
        for (item of this.response) {
            createChildLessonComplete(item)
        }
    })

    client.open("GET", "https://localhost:8443/CiberGym/api/lesson/LessonFilters?name=" + name + "&placesUnder=" + comparePlacesUnder + "&placesOver=" + comparePlacesOver + "&begginingHourBefore=" + comparebegHourBefore + "&begginingHourAfter=" + comparebegHourAfter + "&endingHourBefore=" + compareendHourBefore + "&endingHourAfter=" + compareendHourAfter);
    client.send();

}

function createChildLessonReduce(item, status) {

    var tbody = document.getElementById('tableBody')
    var tr = document.createElement('tr')

    var itemName = document.createTextNode(item.name)
    var nameTd = document.createElement('td');
    nameTd.appendChild(itemName);

    var descriptionTd = document.createElement('td');
    descriptionTd.innerHTML = item.description;

    var itemDetails = document.createElement('input');
    itemDetails.type = 'button';
    itemDetails.id = 'secondInput'
    itemDetails.onclick = function () {
        window.location = "/api/lesson/getLessonRest.html?id=" + item.id;
    }
    itemDetails.value = "Details";
    var detailsTd = document.createElement('td');
    detailsTd.appendChild(itemDetails);

    if (status == 200) {
        var itemModify = document.createElement('input');
        itemModify.type = 'button';
        itemModify.id = 'secondInput'
        itemModify.onclick = function () {
            window.location = "/api/lesson/putLessonRest.html?id=" + item.id;
        }
        itemModify.value = "Modify";
        var modifyTd = document.createElement('td');
        modifyTd.appendChild(itemModify);

        var itemDelete = document.createElement('input');
        itemDelete.type = 'button';
        itemDelete.id = 'secondInput'
        itemDelete.onclick = function () {
            deleteLesson(item.id)
        }
        itemDelete.value = "Delete";
        var deleteTd = document.createElement('td');
        deleteTd.appendChild(itemDelete);
    }

    var itemReservedMember = document.createElement('input');
    itemReservedMember.type = 'button';
    itemReservedMember.id = 'secondInput'
    itemReservedMember.onclick = function () {
        window.location = "/api/lesson/getMemberByLesson.html?id=" + item.id;
    }
    itemReservedMember.value = "Get Members";
    var itemReservedMemberTd = document.createElement('td');
    itemReservedMemberTd.appendChild(itemReservedMember);

    tr.appendChild(nameTd);
    tr.appendChild(descriptionTd);
    tr.appendChild(detailsTd);
    if (status == 200) {
        tr.appendChild(modifyTd);
        tr.appendChild(deleteTd);
    }
    tr.appendChild(itemReservedMemberTd);
    tbody.appendChild(tr);
}

function createChildLessonComplete(item) {

    var tbody = document.getElementById('tableBody')
    var tr = document.createElement('tr')

    var itemName = document.createTextNode(item.name)
    var nameTd = document.createElement('td');
    nameTd.appendChild(itemName);

    var descriptionTd = document.createElement('td');
    descriptionTd.innerHTML = item.description;

    var itemPlaces = document.createTextNode(item.availablePlaces)
    var placesTd = document.createElement('td');
    placesTd.appendChild(itemPlaces);

    var itemBeggining = document.createTextNode(item.begginingHour + ":" + item.begginingMinutes)
    var begginingTd = document.createElement('td');
    begginingTd.appendChild(itemBeggining);

    var itemEnding = document.createTextNode(item.endingHour + ":" + item.endingMinutes)
    var endingTd = document.createElement('td');
    endingTd.appendChild(itemEnding);

    tr.appendChild(nameTd);
    tr.appendChild(descriptionTd);
    tr.appendChild(placesTd);
    tr.appendChild(begginingTd);
    tr.appendChild(endingTd);
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