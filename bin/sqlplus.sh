bin_dir=$(cd $(dirname $0) && pwd)
container_name=dbserver
. $bin_dir/../docker/.env
cd $bin_dir/../docker && docker-compose exec $container_name sqlplus testuser/$USER_PASS@localhost:1521/XE
