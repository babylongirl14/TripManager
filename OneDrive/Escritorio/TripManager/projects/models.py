from django.db import models
from django.contrib.auth.models import User


class Trip(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name="trips")
    destino = models.CharField(max_length=100)
    fecha_inicio = models.DateField()
    fecha_fin = models.DateField()
    tipo = models.CharField(
        max_length=20,
        choices=[("Vacaciones", "Vacaciones"), ("Trabajo", "Trabajo")]
    )

    def __str__(self):
        return f"{self.destino} ({self.user.username})"


class Activity(models.Model):
    trip = models.ForeignKey(Trip, on_delete=models.CASCADE, related_name="activities")
    descripcion = models.TextField()
    hora = models.DateTimeField()
    recordatorio = models.CharField(max_length=50, blank=True, null=True)
    alerta = models.CharField(
        max_length=20,
        choices=[("Normal", "Normal"), ("Importante", "Importante")]
    )

    def __str__(self):
        return f"{self.descripcion[:30]} - {self.trip.destino}"
