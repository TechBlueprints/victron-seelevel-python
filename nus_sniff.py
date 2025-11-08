import asyncio
import sys
import time

ADDR = "05:23:01:64:00:C3"


def ensure_bleak_on_path() -> None:
    try:
        import bleak  # noqa: F401
        return
    except Exception:
        pass
    sys.path.insert(0, "/data/apps/dbus-serialbattery/ext")
    sys.path.insert(0, "/data/apps/dbus-serialbattery/ext/bleak")


async def sniff(adapter: str | None, seconds: float) -> None:
    from bleak import BleakClient

    label = adapter if adapter else "default"
    print(f"=== adapter: {label} ===", flush=True)
    try:
        client = BleakClient(ADDR, adapter=adapter, timeout=12.0)
        await client.connect()
        print("connected", flush=True)

        NUS_RX = "6e400003-b5a3-f393-e0a9-e50e24dcca9e"
        last = time.time()

        def cb(_sender, data: bytearray):
            now = time.time()
            delta = now - last
            print(f"NOTIFY dt={delta:.3f}s len={len(data)} {data.hex()}", flush=True)

        await client.start_notify(NUS_RX, cb)
        await asyncio.sleep(seconds)
        await client.stop_notify(NUS_RX)
        await client.disconnect()
    except Exception as e:
        print(f"ERROR on {label}: {e}")


async def main() -> None:
    ensure_bleak_on_path()
    for adapter in ["hci0", "hci1", None]:
        await sniff(adapter, seconds=20.0)


if __name__ == "__main__":
    asyncio.run(main())


