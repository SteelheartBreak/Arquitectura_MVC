from fastapi import FastAPI, HTTPException,Path
import cx_Oracle
import uvicorn

# Configuración de la conexión a la base de datos
db_user = "SYSTEM"
db_password = "pass"
db_host = "localhost"
db_port = "1521"
db_service = "XE"

# Conexión a la base de datos
try:
    connection = cx_Oracle.connect(
        user=db_user,
        password=db_password,
        dsn=f"{db_host}:{db_port}/{db_service}"
    )
    cursor = connection.cursor()
except cx_Oracle.Error as error:
    print("Error de conexión:", error)
    raise

# Definir la aplicación FastAPI
app = FastAPI()

# Ruta de ejemplo para obtener datos de la base de datos
@app.get("/datos")
async def obtener_datos():
    try:
        # Ejemplo de consulta a la base de datos
        cursor.execute("SELECT * FROM usuario")
        datos = cursor.fetchall()
        return {"datos": datos}
    except cx_Oracle.Error as error:
        print("Error al obtener datos:", error)
        raise HTTPException(status_code=500, detail="Error al obtener datos de la base de datos")

# Endpoint para eliminar datos por nombre de usuario
@app.get("/eliminar/{nombre_usuario}")
async def eliminar_datos_por_nombre(nombre_usuario: str):
    try:
        # Verificar si el usuario existe
        cursor.execute("SELECT COUNT(*) FROM usuario WHERE nombre_usuario = :nombre_usuario",
                       {"nombre_usuario": nombre_usuario})
        count = cursor.fetchone()[0]
        if count == 0:
            raise HTTPException(status_code=404, detail=f"El usuario no existe")

        # Eliminar el usuario
        cursor.execute("DELETE FROM usuario WHERE nombre_usuario = :nombre_usuario", {"nombre_usuario": nombre_usuario})
        connection.commit()
        return {"message": f"Usuario eliminado correctamente"}
    except cx_Oracle.Error as error:
        print("Error al eliminar datos:", error)
        raise HTTPException(status_code=500, detail="Error al eliminar datos de la base de datos")


# Ruta para buscar un usuario por nombre
@app.get("/buscar/{nombre_usuario}")
async def buscar_usuario_por_nombre(nombre_usuario: str):
    try:
        # Ejemplo de consulta para buscar un usuario por nombre
        cursor.execute("SELECT * FROM usuario WHERE nombre_usuario = :nombre_usuario", {"nombre_usuario": nombre_usuario})
        usuario = cursor.fetchone()
        if usuario:
            return {"Usuario encontrado"}
        else:
            raise HTTPException(status_code=404, detail="El usuario no existe")
    except cx_Oracle.Error as error:
        print("Error al buscar usuario:", error)
        raise HTTPException(status_code=500, detail="Error al buscar usuario en la base de datos")

# Ruta para crear un nuevo usuario
@app.get("/crear_usuario/{nombre_usuario}")
async def crear_usuario_endpoint(nombre_usuario: str):
    try:
        # Verificar si el usuario ya existe
        cursor.execute("SELECT COUNT(*) FROM usuario WHERE nombre_usuario = :nombre_usuario",
                       {"nombre_usuario": nombre_usuario})
        count = cursor.fetchone()[0]
        if count > 0:
            raise HTTPException(status_code=400, detail=f"El usuario ya existe")

        # Insertar el nuevo usuario en la base de datos
        cursor.execute("INSERT INTO usuario (nombre_usuario) VALUES (:nombre_usuario)",
                       {"nombre_usuario": nombre_usuario})
        connection.commit()
        return {"message": f"Usuario creado correctamente"}
    except cx_Oracle.Error as error:
        print("Error al crear usuario:", error)
        raise HTTPException(status_code=500, detail="Error al crear usuario en la base de datos")


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
