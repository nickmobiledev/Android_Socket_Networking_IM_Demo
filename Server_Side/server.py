import socket 
import re
import signal
import sys
import time
import threading
import json


class ClientListener(threading.Thread):
	def __init__(self, server, socket, address):
		super(ClientListener, self).__init__()
		self.server = server
		self.address = address
		self.socket = socket
		self.listening = True
		self.username = "No Username"

	def run(self):
		while self.listening:
			data = ""
			try:
				data = self.socket.recv(1024)
			except socket.error:
				print("Unable to recieve data")
			self.handle_msg(data)
			time.sleep(0.1)
		print("Ending client thread for {0}".format(self.address))

	def quit(self):
		print("Username " + str(self.username) + " QUIT")
		self.listening = False
		try:
			self.socket.close()
		except:
			print("Socket Error, Socket Not Closed")
		self.server.remove_socket(self.socket)


	def handle_msg(self, data):
		print("data type = " + str(type(data)))
		try:
			data = data.decode()
		except:
			data = data
		if data == "":
			self.quit()
		else:	
			self.server.echo(data)

		

class Server():
	def __init__(self, port):
		self.listener = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		self.listener.bind(('', port))
		self.listener.listen(0)
		print("Listening on port {0}".format(port))
		self.client_sockets = []
		self.client_addresses = []
		signal.signal(signal.SIGINT, self.signal_handler)
		signal.signal(signal.SIGTERM, self.signal_handler)

	def run(self):
		while True:
			print("Listening for more clients")
			try:
				(client_socket, client_address) = self.listener.accept()
			except socket.error:
				print("Could not accept anymore connections")
				print("Waiting For An Socket Open Socket")
				time.sleep(1)
			self.client_sockets.append(client_socket)
			print("Starting client thread for {0}".format(client_address))
			client_thread = ClientListener(self, client_socket, client_address)
			client_thread.start()
			time.sleep(0.1)

	def echo(self, data):
		print("Echoing {0}".format(data))
		for socket in self.client_sockets:
			try:
				socket.sendall(data.encode("utf-8"))
			except socket.error:
				print("unable to send message")

	def remove_socket(self, socket):
		try:
			self.client_sockets.remove(socket)
		except:
			print("Couldn't remove socket from list");

	def signal_handler(self, signal, frame):
		print("Tidying up")
		self.listener.close()
		self.echo("QUIT")


server = Server(59117)
server.run()












