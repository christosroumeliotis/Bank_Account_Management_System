#!/bin/bash
# wait-for-it.sh

# Usage: ./wait-for-it.sh host:port -- command
host=$1
port=$2
shift 2

until nc -z "$host" "$port"; do
  echo "Waiting for $host:$port to be ready..."
  sleep 2
done

echo "$host:$port is ready. Running command..."
exec "$@"