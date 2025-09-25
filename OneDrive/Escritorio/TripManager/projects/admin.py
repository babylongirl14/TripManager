from django.contrib import admin
from .models import Trip, Activity

@admin.register(Trip)
class TripAdmin(admin.ModelAdmin):
    list_display = ('destino', 'tipo', 'fecha_inicio', 'fecha_fin', 'user')
    list_filter = ('tipo', 'fecha_inicio')
    search_fields = ('destino', 'user__username')

@admin.register(Activity)
class ActivityAdmin(admin.ModelAdmin):
    list_display = ('trip', 'descripcion', 'hora', 'alerta')
    list_filter = ('alerta', 'hora')
    search_fields = ('descripcion', 'trip__destino')
