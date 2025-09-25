from rest_framework import serializers
from .models import Trip, Activity


class ActivitySerializer(serializers.ModelSerializer):
    class Meta:
        model = Activity
        fields = ["id", "trip", "descripcion", "hora", "recordatorio", "alerta"]


class TripSerializer(serializers.ModelSerializer):
    # Agregamos las actividades relacionadas
    activities = ActivitySerializer(many=True, read_only=True)

    class Meta:
        model = Trip
        fields = ["id", "destino", "fecha_inicio", "fecha_fin", "tipo", "user", "activities"]
        read_only_fields = ["user"]  # El usuario se asigna automáticamente
