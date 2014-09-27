import MySQLdb

db = MySQLdb.connect(host="localhost", user="root", passwd="next123!@#", db="URLShorterBackup")
insert = True

f = open('/home/next/redis-2.8.14/src/appendonly.aof', 'r')
cur = db.cursor()
cur.execute("DELETE FROM Backup;")
db.commit()

line = f.readline()
while(line !=""):
	line = line.upper()
	line = line.replace('\r\n', '')
	if(line == 'SET'):
		f.readline()
		key = (f.readline()).replace('\r\n','')
		f.readline()
		value = (f.readline()).replace('\r\n','')

		if(insert == True):
			cur.execute("INSERT INTO Backup VALUES (%s, %s)", (key, value))
			db.commit()
			insert = False
		else:
			insert = True
	line = f.readline()

db.close()
