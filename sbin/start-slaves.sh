#!/usr/bin/env bash


if [ -z "${MOONBOX_HOME}" ]; then
  export MOONBOX_HOME="$(cd "`dirname "$0"`/.."; pwd)"
fi

if [ -f "${MOONBOX_HOME}/conf/moonbox-env.sh" ]; then
  . "${MOONBOX_HOME}/conf/moonbox-env.sh"
fi

if [ "$MOONBOX_MASTER_PORT" == "" ]; then
  MOONBOX_MASTER_PORT=2551
fi

if [ "$MOONBOX_MASTER_HOST" == "" ]; then
  case `uname` in
      (SunOS)
	  MOONBOX_MASTER_HOST="`/usr/sbin/check-hostname | awk '{print $NF}'`"
	  ;;
      (*)
	  MOONBOX_MASTER_HOST="`hostname -f`"
	  ;;
  esac
fi

"${MOONBOX_HOME}/sbin/slaves.sh" cd "${MOONBOX_HOME}" \; "${MOONBOX_HOME}/sbin/start-slave.sh" "moonbox://$MOONBOX_MASTER_HOST:$MOONBOX_MASTER_PORT"
