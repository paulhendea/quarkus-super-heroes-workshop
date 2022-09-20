import psycopg2
try:
    db = psycopg2.connect("dbname='villains_database' user='superbad' host='localhost:5432' password='superbad'")
except:
    print('FAIL')
    exit(1)

print('OK')
exit(0)