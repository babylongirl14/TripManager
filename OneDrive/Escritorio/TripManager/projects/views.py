from django.contrib.auth import authenticate
from rest_framework.decorators import api_view, action
from rest_framework.response import Response
from rest_framework import status, viewsets
from rest_framework.permissions import IsAuthenticated
from .models import Trip, Activity
from .serializers import TripSerializer, ActivitySerializer


# ---- API para Trip ----
class TripViewSet(viewsets.ModelViewSet):
    serializer_class = TripSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        # Solo devuelve los viajes del usuario autenticado
        return Trip.objects.filter(user=self.request.user)

    def perform_create(self, serializer):
        # Asigna el usuario logueado al nuevo viaje
        serializer.save(user=self.request.user)


# ---- API para Activity ----
class ActivityViewSet(viewsets.ModelViewSet):
    serializer_class = ActivitySerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        # Solo actividades de los viajes del usuario autenticado
        return Activity.objects.filter(trip__user=self.request.user)

    # 🔹 Endpoint para obtener actividades de un viaje específico
    @action(detail=False, methods=['get'], url_path=r'by-trip/(?P<trip_id>\d+)')
    def by_trip(self, request, trip_id=None):
        actividades = Activity.objects.filter(trip__id=trip_id, trip__user=request.user)
        serializer = self.get_serializer(actividades, many=True)
        return Response(serializer.data)


# ---- Login API simple (opcional, ya que tienes JWT en /api/token/) ----
@api_view(['POST'])
def login_view(request):
    username = request.data.get("username")
    password = request.data.get("password")

    if not username or not password:
        return Response(
            {"success": False, "error": "Se requiere username y password"},
            status=status.HTTP_400_BAD_REQUEST
        )

    user = authenticate(username=username, password=password)
    if user is not None:
        return Response({
            "success": True,
            "user_id": user.id,
            "username": user.username
        }, status=status.HTTP_200_OK)
    else:
        return Response(
            {"success": False, "error": "Credenciales inválidas"},
            status=status.HTTP_400_BAD_REQUEST
        )
