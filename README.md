# Websocket_Chatclient

## Introduction

> This is a re-coded Version of our ChatClient, which has a much better structure than the old one.

## Code Samples

> Login: <br> `` {"typ": "login", "username": "basti", "password": "123"}  `` <p>
Logout: <br> `` {"typ": "logout", "username": "basti"}  `` <p>
Add Group: <br> ``{"typ": "addGroup", "group": "Schule"} `` <p>
Join Group: <br> ``{"typ": "joinGroup", "group": "Schule"}`` <p>
Message: <br> ``{"typ": "data", "group": "Schule", "payload": "Hallo"}`` <p>
Get Groups: <br> ``{"typ":"groups"}`` <p>

## Installation

> Just clone ;)
