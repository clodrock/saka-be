const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Bağlandı: ' + frame);
    stompClient.subscribe('/topic/positive', (greeting) => {
        showPositive(JSON.parse(greeting.body).content);
    });
    stompClient.subscribe('/topic/negative', (greeting) => {
        showNegative(JSON.parse(greeting.body).content);
    });
    stompClient.subscribe('/topic/complaint', (greeting) => {
        showComplaint(JSON.parse(greeting.body).content);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Bağlantı kesildi!");
}

function sendPositive() {
    stompClient.publish({
        destination: "/app/send-positive",
        body: JSON.stringify({'content': $("#positive").val()})
    });
}

function sendNegative() {
    stompClient.publish({
        destination: "/app/send-negative",
        body: JSON.stringify({'content': $("#negative").val()})
    });
}

function sendComplaint() {
    stompClient.publish({
        destination: "/app/send-complaint",
        body: JSON.stringify({'content': $("#complaint").val()})
    });
}

function showPositive(message) {
    $("#positives").append("<tr><td>" + message + "</td></tr>");
}

function showNegative(message) {
    $("#negatives").append("<tr><td>" + message + "</td></tr>");
}

function showComplaint(message) {
    $("#complaints").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#sendPositive" ).click(() => sendPositive());
    $( "#sendNegative" ).click(() => sendNegative());
    $( "#sendComplaint" ).click(() => sendComplaint());
});