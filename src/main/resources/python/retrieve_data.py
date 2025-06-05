# retrieve_data.py
import asyncio
import sys
import json
import time
from bleak import BleakScanner

def parse_smartchef_payload(data: bytes):
    try:
        if len(data) < 6:
            return {"error": "Payload too short"}

        b04 = data[4]
        b05 = data[5]

        weight_raw = b04 * 256 + b05
        weight = weight_raw / 100.0  # Convert to grams

        return {
            "timestamp": int(time.time()),
            "weight": round(weight, 2),
            "raw": data.hex()
        }
    except Exception as e:
        return {
            "timestamp": int(time.time()),
            "error": f"Parse failed: {str(e)}"
        }

async def scan_for_smartchef(target_addr):
    def detection_callback(device, adv_data):
        if device.address.upper() == target_addr.upper():
            for _, payload in adv_data.manufacturer_data.items():
                parsed = parse_smartchef_payload(payload)
                print(json.dumps(parsed))

    scanner = BleakScanner(detection_callback)
    await scanner.start()
    try:
        while True:
            await asyncio.sleep(1)
    finally:
        await scanner.stop()

if __name__ == "__main__":
    if len(sys.argv) < 2:
        sys.exit(1)

    target_mac = sys.argv[1]
    asyncio.run(scan_for_smartchef(target_mac))
